package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.SupplierArrivalPlan;

import java.sql.Date;
import java.util.List;

/**
 * 供应商到货计划服务接口
 * @author wj
 * @version 1.0
 */
public interface SupplierArrivalPlanService {

    /**
     * 获取某天材料的计划到货数量
     * @param material 料号
     * @param date 日期
     * @return 划到货数量
     */
    Double getMaterialArrivalPlanQty(String material, Date date);

    /**
     * 获取时间段内的材料计划到货数量
     * @param fromDate 开始日期
     * @param toDate 结束日期
     * @param material 料号
     * @return List<SupplierArrivalPlan>
     */
    List<SupplierArrivalPlan> getSupplierArrivalPlan(Date fromDate, Date toDate, String material);

    /**
     * 获取时间段内的材料计划到货数量
     * @param fromDate 开始日期
     * @param toDate 结束日期
     * @return List<SupplierArrivalPlan>
     */
    List<SupplierArrivalPlan> getSupplierArrivalPlan(Date fromDate, Date toDate);
}
