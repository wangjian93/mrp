package com.ivo.mrp.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * 到货计划服务接口
 * @author wj
 * @version 1.0
 */
public interface ArrivalPlanService {

    /**
     * 获取到货数量
     * @param fab 厂别
     * @param material 料号
     * @param fabDate 日期
     * @return Double
     */
    double getArrivalPlan(String fab, String material, java.sql.Date fabDate);

    /**
     * 获取到货数量
     * @param fab 厂别
     * @param material 料号
     * @param fabDateList 日期集合
     * @return Map<Date, Double>
     */
    Map<Date, Double> getArrivalPlan(String fab, String material, List<Date> fabDateList);

    /**
     * 获取包材的到货数量
     * 获取包材分配数量
     * @param fab 厂别
     * @param product 机种
     * @param type 单连片
     * @param linkQty 连片数
     * @param material 料号
     * @param fabDate 日期
     * @return double
     */
    double getArrivalPlanPackage(String fab, String product, String type, Double linkQty, String mode, String material, java.sql.Date fabDate);
}
