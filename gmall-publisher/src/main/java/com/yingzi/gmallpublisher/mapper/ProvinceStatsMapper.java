package com.yingzi.gmallpublisher.mapper;

/**
 * @Author: yingzi
 * @Date: 2022/7/27 14:57
 * @Version 1.0
 */

import com.yingzi.gmallpublisher.bean.ProvinceStats;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Desc：地区维度统计Mapper
 */
public interface ProvinceStatsMapper {
    //按地区查询交易额
    @Select("select province_name,sum(order_amount) order_amount " +
            "from province_stats" +
            " where toYYYYMMDD(stt)=#{date}" +
            " group by province_id ,province_name")
    List<ProvinceStats> selectProvinceStats(int date);
}
