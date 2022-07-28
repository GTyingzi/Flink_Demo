package com.yingzi.gmall.realtime.bean;

/**
 * @Author: yingzi
 * @Date: 2022/7/22 16:22
 * @Version 1.0
 */

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Desc: 访客统计实体类 包括各个维度和度量
 */
@Data
@AllArgsConstructor
public class VisitorStats {
    //统计开始时间
    private String stt;
    //统计结束时间
    private String edt;
    //维度：版本
    private String vc;
    //维度：渠道
    private String ch;
    //维度：地区
    private String ar;
    //维度：新老用户标识
    private String is_new;
    //度量：独立访客数
    private Long uv_ct = 0L;
    //度量：页面访问数
    private Long pv_ct = 0L;
    //度量： 进入次数
    private Long sv_ct = 0L;
    //度量： 跳出次数
    private Long uj_ct = 0L;
    //度量： 持续访问时间
    private Long dur_sum = 0L;
    //统计时间
    private Long ts;

}
