package com.yingzi.gmallpublisher.bean;

/**
 * @Author: yingzi
 * @Date: 2022/7/24 14:14
 * @Version 1.0
 */

import com.yingzi.gmall.realtime.bean.TransientSink;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductStats {

    String stt;//窗口起始时间
    String edt;  //窗口结束时间
    Long sku_id; //sku编号
    String sku_name;//sku名称
    BigDecimal sku_price; //sku单价
    Long spu_id; //spu编号
    String spu_name;//spu名称
    Long tm_id; //品牌编号
    String tm_name;//品牌名称
    Long category3_id;//品类编号
    String category3_name;//品类名称

    @Builder.Default
    Long display_ct = 0L; //曝光数

    @Builder.Default
    Long click_ct = 0L;  //点击数

    @Builder.Default
    Long favor_ct = 0L; //收藏数

    @Builder.Default
    Long cart_ct = 0L;  //添加购物车数

    @Builder.Default
    Long order_sku_num = 0L; //下单商品个数

    //下单商品金额
    String order_amount;

    @Builder.Default
    Long order_ct = 0L; //订单数

    @Builder.Default   //支付金额
            BigDecimal payment_amount = BigDecimal.ZERO;

    @Builder.Default
    Long paid_order_ct = 0L;  //支付订单数

    @Builder.Default
    Long refund_order_ct = 0L; //退款订单数

    @Builder.Default
    BigDecimal refund_amount = BigDecimal.ZERO;

    @Builder.Default
    Long comment_ct = 0L;//评论订单数

    @Builder.Default
    Long good_comment_ct = 0L; //好评订单数

    @Builder.Default
    @TransientSink
    Set orderIdSet = new HashSet();  //用于统计订单数

    @Builder.Default
    @TransientSink
    Set paidOrderIdSet = new HashSet(); //用于统计支付订单数

    @Builder.Default
    @TransientSink
    Set refundOrderIdSet = new HashSet();//用于退款支付订单数

    Long ts; //统计时间戳
}
