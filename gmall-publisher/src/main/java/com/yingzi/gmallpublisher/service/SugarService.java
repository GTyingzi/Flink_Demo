package com.yingzi.gmallpublisher.service;

import com.yingzi.gmallpublisher.bean.ProductStats;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author: yingzi
 * @Date: 2022/7/25 23:01
 * @Version 1.0
 */
public interface SugarService {

    BigDecimal getGmv(int date);

    //统计某天不同品牌商品交易额排名
    Map getGmvByTm(int date, int limit);

    //统计某天不同类别商品交易额排名
    Map getGmvByC3(int date, int limit);

    //统计某天不同SPU商品交易额排名
    List<ProductStats> getGmvBySpu(int date,int limit);

}
