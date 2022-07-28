package com.yingzi.gmallpublisher.service.impl;

import com.yingzi.gmallpublisher.bean.VisitorStats;
import com.yingzi.gmallpublisher.mapper.VisitorStatsMapper;
import com.yingzi.gmallpublisher.service.VisitorStatsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * @Author: yingzi
 * @Date: 2022/7/27 15:54
 * @Version 1.0
 */
@Service
public class VisitorStatsServiceImpl implements VisitorStatsService {

    @Autowired
    VisitorStatsMapper visitorStatsMapper;

    @Override
    public List<VisitorStats> getVisitorStatsByNewFlag(int date) {
        return visitorStatsMapper.selectVisitorStatsByNewFlag(date);
    }

    @Override
    public List<VisitorStats> getVisitorStatsByHour(int date) {
        return visitorStatsMapper.selectVisitorStatsByHour(date);
    }

    @Override
    public Long getPv(int date) {
        return visitorStatsMapper.selectPv(date);
    }

    @Override
    public Long getUv(int date) {
        return visitorStatsMapper.selectUv(date);
    }
}
