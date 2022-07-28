package com.yingzi.gmall.realtime.app.function;

import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;

/**
 * @Author: yingzi
 * @Date: 2022/7/21 14:28
 * @Version 1.0
 */
public interface DimAsyncJoinFunction<T> {


    String getKey(T input);

    void join(T input, JSONObject dimInfo) throws ParseException;
}
