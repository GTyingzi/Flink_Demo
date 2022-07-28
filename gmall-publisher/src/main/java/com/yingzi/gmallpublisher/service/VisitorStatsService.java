package com.yingzi.gmallpublisher.service;

import com.yingzi.gmallpublisher.bean.VisitorStats;

import java.util.List;

/**
 * @Author: yingzi
 * @Date: 2022/7/27 15:50
 * @Version 1.0
 */
public interface VisitorStatsService {

    List<VisitorStats> getVisitorStatsByNewFlag(int date);

    List<VisitorStats> getVisitorStatsByHour(int date);

    Long getPv(int date);

    Long getUv(int date);
}
