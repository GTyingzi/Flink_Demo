package com.yingzi.gmallpublisher.mapper;

import com.yingzi.gmallpublisher.bean.ProductStats;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author: yingzi
 * @Date: 2022/7/25 23:02
 * @Version 1.0
 */
public interface ProductStatusMapper {

    @Select("select sum(order_amount) from product_stats where toYYYYMMDD(stt)=#{date}")
    BigDecimal selectGmv(int date);

    /**
     * ┌─tm_name─┬─order_amount─┐
     * │ 华为     │     90765.00 │
     * │ 苹果     │     73773.00 │
     * │ TCL     │     56893.00 │
     * │ 三星     │     23488.00 │
     * │ 小米     │      8697.00 │
     * └─────────┴──────────────┘
     * Map[("tm_name"->"华为"),(order_amount->90765)]  ==> [("华为"->90765),...]
     */
    //统计某天不同品牌商品交易额排名
    @Select("select tm_name,sum(order_amount) order_amount " +
            "from product_stats " +
            "where toYYYYMMDD(stt)=#{date} " +
            "group by tm_name order by order_amount desc limit #{limit}")
    List<Map> selectGmvByTm(@Param("date") int date, @Param("limit") int limit);

    //统计某天不同类别商品交易额排名
    @Select("select category3_name,sum(order_amount) order_amount " +
            "from product_stats " +
            "where toYYYYMMDD(stt)=#{date} " +
            "group by category3_name order by order_amount desc limit #{limit}")
    List<Map> selectGmvByC3(@Param("date") int date, @Param("limit") int limit);

    //统计某天不同SPU商品交易额排名
    @Select("select spu_id,spu_name,order_amount,sum(product_stats.order_ct) order_ct " +
            "from product_stats " +
            "where toYYYYMMDD(stt)=#{date} " +
            "group by spu_id,spu_name order by sum(product_stats.order_amount) as order_amount desc limit #{limit} ")

    List<ProductStats> selectGmvBySpu(@Param("date") int date, @Param("limit") int limit);


}
