package com.ivo.mrp.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * 需求量分配服务接口
 * @author wj
 * @version 1.0
 */
public interface AllocationService {

    /**
     * 获取分配数量
     * @param fab 厂别
     * @param material 料号
     * @param fabDate 日期
     * @return Double
     */
    double getAllocation(String fab, String material, java.sql.Date fabDate);

    /**
     * 获取分配数量
     * @param fab 厂别
     * @param material 料号
     * @param fabDateList 日期集合
     * @return  Map<Date, Double>
     */
    Map<Date, Double> getAllocation(String fab, String material, List<Date> fabDateList);

    /**
     * 获取包材分配数量
     * @param fab 厂别
     * @param product 机种
     * @param type 单连片
     * @param linkQty 连片数
     * @param material 料号
     * @param fabDate 日期
     * @return double
     */
    double getAllocationPackage(String fab, String product, String type, Double linkQty, String mode, String material, java.sql.Date fabDate);

}
