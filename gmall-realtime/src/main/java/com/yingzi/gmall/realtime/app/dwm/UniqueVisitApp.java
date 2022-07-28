package com.yingzi.gmall.realtime.app.dwm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.inject.internal.util.$StackTraceElements;
import com.yingzi.gmall.realtime.utils.MyKafkaUtil;
import org.apache.flink.api.common.functions.RichFilterFunction;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.text.SimpleDateFormat;

/**
 * @Author: yingzi
 * @Date: 2022/7/19 11:48
 * @Version 1.0
 */

//数据流：web/app -> Nginx -> SpringBoot -> Kafka(ods) -> FlinkApp -> Kafka(dwd) -> FlinkApp -> kafka(dwm)
//程序：mocklog -> Nginx -> Logger.sh -> Kafka(ZK) -> BaseLogApp -> kafka -> UniqueVistApp -> Kafka
public class UniqueVisitApp {

    public static void main(String[] args) throws Exception {
        //TODO 1、获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //1.1 开启CK并指定状态后端为fs
//        env.getCheckpointConfig().setCheckpointStorage(new FileSystemCheckpointStorage("hdfs://hadoop102:8020/gmall/ck"));
//
//
//        env.enableCheckpointing(5000);
//        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
//        env.getCheckpointConfig().setCheckpointTimeout(10000L);
//        env.getCheckpointConfig().setMaxConcurrentCheckpoints(2);
//        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(3000);

//        env.setRestartStrategy(RestartStrategies.fixedDelayRestart());

        //TODO 2、读取Kagka dwd_page_log 主题的数据
        String groupId = "unique_visit_app";
        String sourceTopic = "dwd_page_log";
        String sinkTopic = "dwm_unique_visit";
        DataStreamSource<String> kafkaDS = env.addSource(MyKafkaUtil.getKafkaConsumer(sourceTopic, groupId));

        //TODO 3、将每行数据转换为JSON对象
        SingleOutputStreamOperator<JSONObject> jsonObjDS = kafkaDS.map(JSON::parseObject);

        //TODO 4、过滤数据 状态编程 只保留每个mid每天第一次登录的数据
        KeyedStream<JSONObject, String> keyedStream = jsonObjDS.keyBy(jsonObj -> jsonObj.getJSONObject("common").getString("mid"));
        SingleOutputStreamOperator<JSONObject> uvDS = keyedStream.filter(new RichFilterFunction<JSONObject>() {

            private ValueState<String> dataState;
            private SimpleDateFormat simpleDateFormat;

            @Override
            public void open(Configuration parameters) throws Exception {
                ValueStateDescriptor<String> stringValueStateDescriptor = new ValueStateDescriptor<>("date-state", String.class);
                //设置状态的超时时间以及更新的方式
                StateTtlConfig stateTtlConfig = new StateTtlConfig
                        .Builder(Time.hours(24))
                        .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
                        .build();
                stringValueStateDescriptor.enableTimeToLive(stateTtlConfig);
                dataState = getRuntimeContext().getState(stringValueStateDescriptor);

                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            }

            @Override
            public boolean filter(JSONObject jsonObject) throws Exception {
                //取出上一条页面信息
                String lastPageId = jsonObject.getJSONObject("page").getString("last_page_id");

                //判断上一条页面是否为null
                if (lastPageId == null || lastPageId.length() <= 0) {
                    //取出状态数据
                    String lateDate = dataState.value();
                    //取出今天的日期
                    String cureDate = simpleDateFormat.format(jsonObject.getLong("ts"));
                    //判断两个日期是否相同
                    if (!cureDate.equals(lateDate)) {
                        dataState.update(cureDate);
                        return true;
                    }
                }
                return false;
            }
        });

        //TODO 5、将数据写入Kafka
        uvDS.print();
        uvDS.map(json -> json.toJSONString())
                .addSink(MyKafkaUtil.getKafkaProducer(sinkTopic));

        //TODO 6、启动任务
        env.execute("UniqueVisitApp");
    }
}
