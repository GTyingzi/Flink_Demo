package com.yingzi.gmall.realtime.bean;

/**
 * @Author: yingzi
 * @Date: 2022/7/21 23:06
 * @Version 1.0
 */

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentInfo {
    Long id;
    Long order_id;
    Long user_id;
    BigDecimal total_amount;
    String subject;
    String payment_type;
    String create_time;
    String callback_time;
}
