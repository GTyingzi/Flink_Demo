package com.yingzi.gmall.realtime.utils;

import com.alibaba.fastjson.JSONObject;
import com.yingzi.gmall.realtime.common.GmallConfig;
import org.apache.kafka.clients.consumer.StickyAssignor;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author: yingzi
 * @Date: 2022/7/19 22:48
 * @Version 1.0
 */
public class DimUtil {

    public static JSONObject getDimInfo(Connection connection, String tableName, String id) throws Exception {

        //查询Phoenix之前先查询Redis
        Jedis jedis = RedisUtil.getJedis();
        //DIM:DIM_USER_INFO:97
        String redisKey = "DIM:" + tableName + ":" + id;
        String dimInfoJsonStr = jedis.get(redisKey);
        if (dimInfoJsonStr != null) {
            //重置过期时间
            jedis.expire(redisKey, 24 * 60 * 60);

            //归还连接
            jedis.close();

            //返还结果
            return JSONObject.parseObject(dimInfoJsonStr);
        }

        //拼接查询语句：select * from db.tn where id ='18'
        String querySql = "select * from " + GmallConfig.HBASE_SCHEMA + "." + tableName +
                " where id ='" + id + "'";

        //查询Phoenix
        List<JSONObject> queryList = JdbcUtil.queryList(connection, querySql, JSONObject.class, false);

        JSONObject dimInfoJson = queryList.get(0);

        //在返回结果之前，将数据写入Redis
        jedis.set(redisKey, dimInfoJson.toJSONString());
        jedis.expire(redisKey, 24 * 60 * 60);
        jedis.close();

        //返回结果
        return dimInfoJson;
    }

    public static void delRedisDimInfo(String tableName, String id) {
        Jedis jedis = RedisUtil.getJedis();
        String redisKey = "DIM:" + tableName + ":" + id;
        jedis.del(redisKey);
        jedis.close();
    }


    public static void main(String[] args) throws Exception {
        Class.forName(GmallConfig.PHOENIX_DRIVER);
        Connection connection = DriverManager.getConnection(GmallConfig.PHOENIX_SERVER);

        long start = System.currentTimeMillis();
        System.out.println(getDimInfo(connection, "DIM_BASE_TRADEMARK", "16"));
        long end1 = System.currentTimeMillis();

        System.out.println(getDimInfo(connection, "DIM_BASE_TRADEMARK", "16"));
        long end2 = System.currentTimeMillis();

        System.out.println(getDimInfo(connection, "DIM_BASE_TRADEMARK", "16"));
        long end3 = System.currentTimeMillis();

        System.out.println(end1 - start);
        System.out.println(end2 - end1);
        System.out.println(end3 - end2);

        connection.close();
    }
}
