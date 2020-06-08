package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.SupplierArrivalPlan;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * 供应商到货计划服务接口
 * @author wj
 * @version 1.0
 */
public interface SupplierArrivalPlanService {

    /**
     * 获取供应商到货计划的数量
     * @param plant 厂别
     * @param material 料号
     * @param date 日期
     * @return 划到货数量
     */
    double getMaterialArrivalPlanQty(String plant, String material, Date date);

    /**
     * 获取供应商的到货计划
     * @param plant 厂别
     * @param material 料号
     * @param date 日期
     * @return  List<SupplierArrivalPlan>
     */
    List<SupplierArrivalPlan> getSupplierArrivalPlan(String plant, String material, Date date);

    /**
     * 保存供应商到货计划
     */
    void saveSupplierArrivalPlan(List<SupplierArrivalPlan> supplierArrivalPlans);

    /**
     * 按条件分页查询供应商到货计划
     * @param page 页数
     * @param limit 分页大小
     * @param plant 厂别
     * @param material 料号
     * @param supplier 供应商
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return Page<Map>
     */
    Page<Map> pageQuerySupplierArrivalPlan(int page, int limit, String plant, String material, String supplier,
                        Date startDate, Date endDate);

    /**
     * 获取供应商每天的计划到料数量
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param plant 厂别
     * @param material 料号
     * @param supplierCode 供应商
     * @return  List<Map>
     */
    List<Map> getSupplierArrivalQty(Date startDate, Date endDate, String plant, String material, String supplierCode);


    /**
     * 获取日期区间内料号的每天的到货数量
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param plant 厂别
     * @param material 料号
     * @return
     */
    List<Map> getDaySupplierArrivalQty(Date startDate, Date endDate, String plant, String material);
}
