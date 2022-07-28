package com.yingzi.Test;

import com.google.inject.internal.cglib.core.$LocalVariablesSorter;
import com.yingzi.gmall.realtime.utils.DateTimeUtil;
import org.apache.kafka.common.protocol.types.Field;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: yingzi
 * @Date: 2022/7/19 21:09
 * @Version 1.0
 */
public class Test1 {

    @Test
    public void test1() throws ParseException {

        String create_time = "1658255598000";//"1658255598000" -> 2020-12-18 15:24:26
        long l = Long.parseLong(create_time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(l);
        System.out.println(format);
    }

    @Test
    public void test2() {
        String birthday = "5274";
        long currenTs = System.currentTimeMillis();
        long day = 1000 * 60 * 60 * 24;
        long day_ts = Long.parseLong(birthday) * day;
        long age = (currenTs - day_ts)/(day * 365L);
        System.out.println("年龄为：" + age);
    }

    @Test
    public void test3(){
//        String create_time1 = "1658255598000";
        String create_time2 = "2020-12-18 15:24:26";

//        Long aLong1 = DateTimeUtil.toTs(create_time1);
        Long aLong2 = DateTimeUtil.toTs(create_time2);

//        System.out.println(aLong1);
        System.out.println(aLong2);

    }

    @Test
    public void test4(){
        Date date = new Date();
        String s = DateTimeUtil.toYMDhms(date);
        System.out.println(s);
    }
}
