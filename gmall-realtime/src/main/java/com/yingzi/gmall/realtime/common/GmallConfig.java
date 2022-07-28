package com.yingzi.gmall.realtime.common;

/**
 * @Author: yingzi
 * @Date: 2022/7/17 17:38
 * @Version 1.0
 */
public class GmallConfig {
    //Phoenix 库名
    public static final String HBASE_SCHEMA = "GMALL2022_REALTIME";
    //Phoenix 驱动
    public static final String PHOENIX_DRIVER = "org.apache.phoenix.jdbc.PhoenixDriver";
    //Phoenix 连接参数
    public static final String PHOENIX_SERVER = "jdbc:phoenix:hadoop102,hadoop103,hadoop104:2181";

    //ClickHouse_Url
    public static final String CLICKHOUSE_URL = "jdbc:clickhouse://hadoop102:8123/default";
    //ClickHouse_Driver
    public static final String CLICKHOUSE_DRIVER = "ru.yandex.clickhouse.ClickHouseDriver";


}
