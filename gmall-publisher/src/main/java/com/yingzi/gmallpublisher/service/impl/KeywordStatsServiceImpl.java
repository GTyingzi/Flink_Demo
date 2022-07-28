package com.yingzi.gmallpublisher.service.impl;

import com.yingzi.gmallpublisher.bean.KeywordStats;
import com.yingzi.gmallpublisher.mapper.KeywordStatsMapper;
import com.yingzi.gmallpublisher.service.KeywordStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: yingzi
 * @Date: 2022/7/27 16:22
 * @Version 1.0
 */
@Service
public class KeywordStatsServiceImpl implements KeywordStatsService {

    @Autowired
    KeywordStatsMapper keywordStatsMapper;

    @Override
    public List<KeywordStats> getKeywordStats(int date, int limit) {
        return keywordStatsMapper.selectKeywordStats(date, limit);
    }
}
