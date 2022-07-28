package com.yingzi;

import com.alibaba.ververica.cdc.connectors.mysql.MySQLSource;
import com.alibaba.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.alibaba.ververica.cdc.debezium.DebeziumSourceFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Author: yingzi
 * @Date: 2022/7/16 10:52
 * @Version 1.0
 */
public class FlinkCDC_CustomerSchema {

    public static void main(String[] args) throws Exception {
        //1、获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //2、通过FlinkCDC构建SourceFunction并读取数据
        DebeziumSourceFunction<String> sourceFunction = MySQLSource.<String>builder()
                .hostname("hadoop102")
                .port(3306)
                .username("root")
                .password("000000")
                .databaseList("gmall")
                .tableList("gmall.base_trademark")//若不添加该数据，则消费指定数据库中所有表的数据，如果指定，指定方式为db.table
                .deserializer(new CustomerSchema())
                .startupOptions(StartupOptions.initial())
                .build();
        DataStreamSource<String> streamSource = env.addSource(sourceFunction);

        //3、打印数据
        streamSource.print();

        //4、启动任务
        env.execute("FlinkCDC_CustomerSchema");
    }
}
