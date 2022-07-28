package com.yingzi.gmallpublisher.controller;

import com.yingzi.gmallpublisher.bean.KeywordStats;
import com.yingzi.gmallpublisher.service.KeywordStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Author: yingzi
 * @Date: 2022/7/27 16:24
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/sugar")
public class KeywordController {

    @Autowired
    private KeywordStatsService keywordStatsService;

    private int getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateTime = sdf.format(System.currentTimeMillis());
        return Integer.parseInt(dateTime);
    }

    @RequestMapping("/keyword")
    public String getKeywordStats(@RequestParam(value = "date", defaultValue = "0") Integer date,
                                  @RequestParam(value = "limit", defaultValue = "20") int
                                          limit) {
        if (date == 0) {
            date = getToday();
        }
        //查询数据
        List<KeywordStats> keywordStatsList = keywordStatsService.getKeywordStats(date, limit);
        StringBuilder jsonSb = new StringBuilder("{\"status\":0,\"msg\":\"\",\"data\":[");
        //循环拼接字符串
        for (int i = 0; i < keywordStatsList.size(); i++) {
            KeywordStats keywordStats = keywordStatsList.get(i);
            if (i >= 1) {
                jsonSb.append(",");
            }
            jsonSb.append("{\"name\":\"" + keywordStats.getKeyword() + "\"," +
                    "\"value\":" + keywordStats.getCt() + "}");
        }
        jsonSb.append("]}");
        return jsonSb.toString();
    }


}
