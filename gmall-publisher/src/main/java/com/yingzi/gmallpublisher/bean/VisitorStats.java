package com.yingzi.gmallpublisher.bean;

/**
 * @Author: yingzi
 * @Date: 2022/7/27 15:45
 * @Version 1.0
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Desc: 访客流量统计实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitorStats {
    private String stt;
    private String edt;
    private String vc;
    private String ch;
    private String ar;
    private String is_new;
    private Long uv_ct = 0L;
    private Long pv_ct = 0L;
    private Long sv_ct = 0L;
    private Long uj_ct = 0L;
    private Long dur_sum = 0L;
    private Long new_uv = 0L;
    private Long ts;
    private int hr;

    //计算跳出率 = 跳出次数*100/访问次数
    public BigDecimal getUjRate() {
        if (uv_ct != 0L) {
            return BigDecimal.valueOf(uj_ct)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(sv_ct), 2, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    //计算每次访问停留时间(秒) = 当日总停留时间（毫秒)/当日访问次数/1000
    public BigDecimal getDurPerSv() {
        if (uv_ct != 0L) {
            return BigDecimal.valueOf(dur_sum)
                    .divide(BigDecimal.valueOf(sv_ct), 0, RoundingMode.HALF_UP)
                    .divide(BigDecimal.valueOf(1000), 1, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    //计算每次访问停留页面数 = 当日总访问页面数/当日访问次数
    public BigDecimal getPvPerSv() {
        if (uv_ct != 0L) {
            return BigDecimal.valueOf(pv_ct)
                    .divide(BigDecimal.valueOf(sv_ct), 2, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }
}


