package com.yingzi.gmallpublisher.service;

/**
 * @Author: yingzi
 * @Date: 2022/7/27 15:06
 * @Version 1.0
 */

import com.yingzi.gmallpublisher.bean.ProvinceStats;

import java.util.List;

/**
 * Desc: 按地区维度统计Service实现
 */
public interface ProvinceStatsService {
    List<ProvinceStats> getProvinceStats(int date);
}
