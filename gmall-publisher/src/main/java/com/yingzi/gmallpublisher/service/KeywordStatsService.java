package com.yingzi.gmallpublisher.service;

import com.yingzi.gmallpublisher.bean.KeywordStats;

import java.util.List;

/**
 * @Author: yingzi
 * @Date: 2022/7/27 16:21
 * @Version 1.0
 */

/**
 * Desc: 关键词统计接口
 */
public interface KeywordStatsService {

    List<KeywordStats> getKeywordStats(int date, int limit);
}
