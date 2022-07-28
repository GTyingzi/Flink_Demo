package com.yingzi.gmallpublisher.controller;

import com.alibaba.fastjson.JSON;
import com.yingzi.gmallpublisher.bean.ProductStats;
import com.yingzi.gmallpublisher.bean.ProvinceStats;
import com.yingzi.gmallpublisher.service.ProvinceStatsService;
import com.yingzi.gmallpublisher.service.SugarService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @Author: yingzi
 * @Date: 2022/7/25 23:00
 * @Version 1.0
 */

@RestController
@RequestMapping("/api/sugar")
public class SugarController {

    @Autowired
    private SugarService sugarService;

    private int getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateTime = sdf.format(System.currentTimeMillis());
        return Integer.parseInt(dateTime);
    }

    @RequestMapping("/gmv")
    public String getGmv(@RequestParam(value = "date", defaultValue = "0") int date) {

        if (date == 0) {
            date = getToday();
        }

        HashMap<String, Object> result = new HashMap<>();
        result.put("status", 0);
        result.put("msg", "");
        result.put("data", sugarService.getGmv(date));

//        return "        { " +
//                "          \"status\": 0, " +
//                "          \"msg\": \"\", " +
//                "          \"data\": " + sugarService.getGmv(date) + " " +
//                "        }";

        return JSON.toJSONString(result);
    }

    @RequestMapping("/tm")
    public String getGmvByTm(@RequestParam(value = "date", defaultValue = "0") int date,
                             @RequestParam(value = "limit", defaultValue = "5") int limit) {

        if (date == 0) {
            date = getToday();
        }
        Map gmvByTm = sugarService.getGmvByTm(date, limit);
        Set keySet = gmvByTm.keySet();
        Collection values = gmvByTm.values();

        return "{ " +
                "  \"status\": 0, " +
                "  \"msg\": \"\", " +
                "  \"data\": { " +
                "    \"categories\": [\"" +
                StringUtils.join(keySet, "\",\"") +
                "\"], " +
                "    \"series\": [ " +
                "      { " +
                "        \"name\": \"商品品牌\", " +
                "        \"data\": [" +
                StringUtils.join(values, ",") +
                "] " +
                "      } " +
                "    ] " +
                "  } " +
                "}";
    }

    @RequestMapping("/C3")
    public String getGmvByC3(@RequestParam(value = "date", defaultValue = "0") int date,
                             @RequestParam(value = "limit", defaultValue = "5") int limit) {
        if (date == 0) {
            date = getToday();
        }

        String head = "{" +
                "\"status\": 0 ," +
                "\"msg\": \"\"," +
                "\"data\": [ ";

        String mid = "";
        Map gmvByC3 = sugarService.getGmvByC3(date, limit);
        Set keySet = gmvByC3.keySet();
        Collection values = gmvByC3.values();
        Iterator key_iterator = keySet.iterator();
        Iterator value_iterator = values.iterator();
        while (key_iterator.hasNext()) {
            Object key = key_iterator.next();
            Object value = value_iterator.next();
            if ("".equals(mid)) {
                mid = mid + "{" +
                        "    \"name\":\"" + key + "\"," +
                        "    \"value\":" + value + "}";
            } else {
                mid = mid + ",{" +
                        "    \"name\":\"" + key + "\"," +
                        "    \"value\":" + value + "}";
            }
        }

        String end = " ] " +
                "}";

        return head + mid + end;
    }

    @RequestMapping("/Spu")
    public String getGmvBySpu(@RequestParam(value = "date", defaultValue = "0") int date,
                              @RequestParam(value = "limit", defaultValue = "10") int limit) {

        if (date == 0) date = getToday();
        List<ProductStats> statsList = sugarService.getGmvBySpu(date, limit);
        //设置表头
        StringBuilder jsonBuilder =
                new StringBuilder(" " +
                        "{\"status\":0,\"data\":{\"columns\":[" +
                        "{\"name\":\"商品名称\",\"id\":\"spu_name\"}," +
                        "{\"name\":\"交易额\",\"id\":\"order_amount\"}," +
                        "{\"name\":\"订单数\",\"id\":\"order_ct\"}]," +
                        "\"rows\":[");
        //循环拼接表体
        for (int i = 0; i < statsList.size(); i++) {
            ProductStats productStats = statsList.get(i);
            if (i >= 1) {
                jsonBuilder.append(",");
            }
            jsonBuilder.append("{\"spu_name\":\"" + productStats.getSpu_name() + "\"," +
                    "\"order_amount\":" + productStats.getOrder_amount() + "," +
                    "\"order_ct\":" + productStats.getOrder_ct() + "}");
        }
        jsonBuilder.append("]}}");
        return jsonBuilder.toString();

    }


}
