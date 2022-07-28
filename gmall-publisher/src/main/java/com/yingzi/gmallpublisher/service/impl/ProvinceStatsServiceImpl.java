package com.yingzi.gmallpublisher.service.impl;

import com.yingzi.gmallpublisher.bean.ProvinceStats;
import com.yingzi.gmallpublisher.mapper.ProvinceStatsMapper;
import com.yingzi.gmallpublisher.service.ProvinceStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: yingzi
 * @Date: 2022/7/27 15:08
 * @Version 1.0
 */
@Service
public class ProvinceStatsServiceImpl implements ProvinceStatsService {

    @Autowired
    ProvinceStatsMapper provinceStatsMapper;

    @Override
    public List<ProvinceStats> getProvinceStats(int date) {
        return provinceStatsMapper.selectProvinceStats(date);
    }
}
