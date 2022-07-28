package com.yingzi.gmallpublisher.controller;

import com.yingzi.gmallpublisher.bean.VisitorStats;
import com.yingzi.gmallpublisher.service.VisitorStatsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yingzi
 * @Date: 2022/7/27 15:57
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/sugar")
public class VisitorController {

    @Autowired
    VisitorStatsService visitorStatsService;

    private int getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateTime = sdf.format(System.currentTimeMillis());
        return Integer.parseInt(dateTime);
    }

    @RequestMapping("/Visitor")
    public String getVisitorStatsByNewFlag(@RequestParam(value = "date", defaultValue = "0") int date) {
        if (date == 0) date = getToday();
        List<VisitorStats> visitorStatsByNewFlag = visitorStatsService.getVisitorStatsByNewFlag(date);
        VisitorStats newVisitorStats = new VisitorStats();
        VisitorStats oldVisitorStats = new VisitorStats();
        //循环把数据赋给新访客统计对象和老访客统计对象
        for (VisitorStats visitorStats : visitorStatsByNewFlag) {
            if (visitorStats.getIs_new().equals("1")) {
                newVisitorStats = visitorStats;
            } else {
                oldVisitorStats = visitorStats;
            }
        }
        //把数据拼接入字符串
        String json = "{\"status\":0,\"data\":{\"combineNum\":1,\"columns\":" +
                "[{\"name\":\"类别\",\"id\":\"type\"}," +
                "{\"name\":\"用户\",\"id\":\"old\"}]," +
                "\"rows\":" +
                "[{\"type\":\"用户数(人)\"," +
                "\"old\":" + oldVisitorStats.getUv_ct() + "}," +
                "{\"type\":\"总访问页面(次)\"," +
                "\"old\":" + oldVisitorStats.getPv_ct() + "}," +
                "{\"type\":\"跳出率(%)\"," +
                "\"old\":" + oldVisitorStats.getUjRate() + "}," +
                "{\"type\":\"平均在线时长(秒)\"," +
                "\"old\":" + oldVisitorStats.getDurPerSv() + "}," +
                "{\"type\":\"平均访问页面数(人次)\"," +
                "\"old\":" + oldVisitorStats.getPvPerSv()
                + "}]}}";
        return json;
    }

    @RequestMapping("/hr")
    public String getMidStatsGroupbyHourNewFlag(@RequestParam(value = "date", defaultValue = "0") Integer date) {
        if (date == 0) date = getToday();
        List<VisitorStats> visitorStatsHrList = visitorStatsService.getVisitorStatsByHour(date);
        //构建 24 位数组
        VisitorStats[] visitorStatsArr = new VisitorStats[24];
        //把对应小时的位置赋值
        for (VisitorStats visitorStats : visitorStatsHrList) {
            visitorStatsArr[visitorStats.getHr()] = visitorStats;
        }
        List<String> hrList = new ArrayList<>();
        List<Long> uvList = new ArrayList<>();
        List<Long> pvList = new ArrayList<>();
        List<Long> newMidList = new ArrayList<>();
        //循环出固定的 0-23 个小时 从结果 map 中查询对应的值
        for (int hr = 0; hr <= 23; hr++) {
            VisitorStats visitorStats = visitorStatsArr[hr];
            if (visitorStats != null) {
                uvList.add(visitorStats.getUv_ct());
                pvList.add(visitorStats.getPv_ct());
                newMidList.add(visitorStats.getNew_uv());
            } else { //该小时没有流量补零
                uvList.add(0L);
                pvList.add(0L);
                newMidList.add(0L);
            }
            //小时数不足两位补零
            hrList.add(String.format("%02d", hr));
        }
        //拼接字符串
        String json = "{\"status\":0,\"data\":{" + "\"categories\":" +
                "[\"" + StringUtils.join(hrList, "\",\"") + "\"],\"series\":[" +
                "{\"name\":\"uv\",\"data\":[" + StringUtils.join(uvList, ",") + "]}," +
                "{\"name\":\"pv\",\"data\":[" + StringUtils.join(pvList, ",") + "]}," +
                "{\"name\":\"新用户\",\"data\":[" + StringUtils.join(newMidList, ",") + "]}]}}";
        return json;
    }


}
