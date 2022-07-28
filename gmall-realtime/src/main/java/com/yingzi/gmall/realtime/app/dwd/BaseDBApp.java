package com.yingzi.gmall.realtime.app.dwd;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.ververica.cdc.connectors.mysql.MySQLSource;
import com.alibaba.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.alibaba.ververica.cdc.debezium.DebeziumSourceFunction;
import com.yingzi.gmall.realtime.app.function.CustomerSchema;
import com.yingzi.gmall.realtime.app.function.DimSinkFunction;
import com.yingzi.gmall.realtime.app.function.TableProcessFunction;
import com.yingzi.gmall.realtime.bean.TableProcess;
import com.yingzi.gmall.realtime.utils.MyKafkaUtil;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.flink.util.OutputTag;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Nullable;

//数据流：web/app -> nginx -> SpringBoot -> Mysql -> FlinkApp -> Kafka(ods) -> FlinkApp -> Kafka(dwd)/Phoenix(dim)
//程序：mockDb -> Mysql -> FlinkCDC -> Kafka(ZK) -> BaseDBApp -> Kafka/Phoenix(hbase,zk,hdfs)
/**
 * @Author: yingzi
 * @Date: 2022/7/17 16:37
 * @Version 1.0
 */
public class BaseDBApp {

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

//        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
//                3,
//                Time.of(10, TimeUnit.SECONDS)
//        ));

        //TODO 2、消费Kafka ods_base_db 主题数据创建流
        String sourceTopic = "ods_base_db";
        String groupId = "base_db_app";
        DataStreamSource<String> kafkaDS = env.addSource(MyKafkaUtil.getKafkaConsumer(sourceTopic, groupId));

        //TODO 3、将每行数据转换为JSON对象并过滤（delete）主流
        SingleOutputStreamOperator<JSONObject> jsonOBjDS = kafkaDS.map(JSON::parseObject).filter(new FilterFunction<JSONObject>() {
            @Override
            public boolean filter(JSONObject jsonObject) throws Exception {
                //取出数据的操作类型
                String type = jsonObject.getString("type");

                return !"delete".equals(type);
            }
        });

        //TODO 4、使用FlinkCDC消费配置表，并处理成广播流
        DebeziumSourceFunction<String> sourceFunction = MySQLSource.<String>builder()
                .hostname("hadoop102")
                .port(3306)
                .username("root")
                .password("000000")
                .databaseList("gmall-realtime")
                .tableList("gmall-realtime.table_process")
                .deserializer(new CustomerSchema())
                .startupOptions(StartupOptions.initial())
                .build();
        DataStreamSource<String> tableProcessStrDS = env.addSource(sourceFunction);
        MapStateDescriptor mapStateDescriptor = new MapStateDescriptor<String, TableProcess>("map-state", String.class, TableProcess.class);
        BroadcastStream<String> broadcastStream = tableProcessStrDS.broadcast(mapStateDescriptor);

        //TODO 5、连接主流和广播流
        BroadcastConnectedStream<JSONObject, String> connectedStream = jsonOBjDS.connect(broadcastStream);

        //TODO 6、处理数据：分流   广播流数据，主流数据（根据广播流数据进行处理）
        OutputTag<JSONObject> hbaseTag = new OutputTag<JSONObject>("hbase-tag") {
        };
        SingleOutputStreamOperator<JSONObject> kafka = connectedStream.process(new TableProcessFunction(hbaseTag, mapStateDescriptor));

        //TODO 7、提取Kafka流数据和HBase流数据
        DataStream<JSONObject> hbase = kafka.getSideOutput(hbaseTag);

        //TODO 8、将Kafak数据写入Kafka主题，将HBae数据写入Phoenix表
        kafka.print("Kafka>>>>>>>>>>>>>>>>");
        hbase.print("HBase>>>>>>>>>>>>>>>>>");

        hbase.addSink(new DimSinkFunction());
        kafka.addSink(MyKafkaUtil.getKafkaProducer(new KafkaSerializationSchema<JSONObject>() {
            @Override
            public ProducerRecord<byte[], byte[]> serialize(JSONObject element, @Nullable Long timestamp) {
                return new ProducerRecord<>(element.getString("sinkTable"),
                        element.getString("after").getBytes());
            }
        }));

        //TODO 9、启动任务
        env.execute("BaseDBApp");
    }
}
