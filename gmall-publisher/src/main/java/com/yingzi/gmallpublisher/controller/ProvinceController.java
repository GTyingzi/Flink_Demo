package com.yingzi.gmallpublisher.controller;

import com.yingzi.gmallpublisher.bean.ProvinceStats;
import com.yingzi.gmallpublisher.service.ProvinceStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Author: yingzi
 * @Date: 2022/7/27 15:31
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/sugar")
public class ProvinceController {

    @Autowired
    private ProvinceStatsService provinceStatsService;

    private int getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateTime = sdf.format(System.currentTimeMillis());
        return Integer.parseInt(dateTime);
    }

    @RequestMapping("/Province")
    public String getProvinceStats(@RequestParam(value = "date", defaultValue = "0") int date) {
        if (date == 0) {
            date = getToday();
        }

        StringBuilder jsonBuilder = new StringBuilder("{\"status\":0,\"data\":{\"mapData\":[");
        List<ProvinceStats> provinceStatsList = provinceStatsService.getProvinceStats(date);
        if (provinceStatsList.size() == 0) {
            jsonBuilder.append("{\"name\":\"北京\",\"value\":0.00}");
        }
        for (int i = 0; i < provinceStatsList.size(); i++) {
            if (i >= 1) {
                jsonBuilder.append(",");
            }
            ProvinceStats provinceStats = provinceStatsList.get(i);
            jsonBuilder.append("{\"name\":\"" + provinceStats.getProvince_name() +
                    "\",\"value\":" + provinceStats.getOrder_amount() + " }");
        }
        jsonBuilder.append("]}}");
        return jsonBuilder.toString();

    }

    @RequestMapping("/test1")
    public String test(@RequestParam(value = "date",defaultValue = "0") int date){

        return "success";
    }
}
