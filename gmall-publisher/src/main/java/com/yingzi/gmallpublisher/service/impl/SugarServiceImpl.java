package com.yingzi.gmallpublisher.service.impl;

import com.yingzi.gmallpublisher.bean.ProductStats;
import com.yingzi.gmallpublisher.mapper.ProductStatusMapper;
import com.yingzi.gmallpublisher.service.SugarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: yingzi
 * @Date: 2022/7/25 23:01
 * @Version 1.0
 */
@Service
public class SugarServiceImpl implements SugarService {

    @Autowired
    private ProductStatusMapper productStatusMapper;

    @Override
    public BigDecimal getGmv(int date) {
        return productStatusMapper.selectGmv(date);
    }

    @Override
    public Map getGmvByTm(int date, int limit) {
        //查询数据
        List<Map> mapList = productStatusMapper.selectGmvByTm(date, limit);

        //创建Map用于存放结果数据
        HashMap<String, BigDecimal> result = new HashMap<>();

        //遍历mapList，将数据取出放入result   Map[("tm_name"->"华为"),(order_amount->90765)]  ==> [("华为"->90765),...]
        for (Map map : mapList) {
            result.put((String) map.get("tm_name"), (BigDecimal) map.get("order_amount"));
        }

        //返回结果集合
        return result;
    }

    @Override
    public Map getGmvByC3(int date, int limit) {
        //查询数据
        List<Map> mapList = productStatusMapper.selectGmvByC3(date, limit);

        //创建Map用于存放结果数据
        HashMap<String, BigDecimal> result = new HashMap<>();
        for (Map map : mapList) {
            result.put((String) map.get("category3_name"), (BigDecimal) map.get("order_amount"));
        }
        //返回结果集合
        return result;
    }

    @Override
    public List<ProductStats> getGmvBySpu(int date, int limit) {
        return productStatusMapper.selectGmvBySpu(date, limit);
    }

}
