package com.ivo.mrp.service;

import com.ivo.mrp.entity.direct.ArrivalPlan;
import org.springframework.data.domain.Page;

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
    double getArrivalPlanQty(String fab, String material, java.sql.Date fabDate);

    /**
     * 获取到货计划
     * @param fab 厂别
     * @param material 料号
     * @param fabDate 日期
     * @return List<ArrivalPlan>
     */
    List<ArrivalPlan> getArrivalPlan(String fab, String material, Date fabDate);


    /**
     * 获取到货数量
     * @param fab 厂别
     * @param material 料号
     * @param fabDateList 日期集合
     * @return Map<Date, Double>
     */
    Map<Date, Double> getArrivalPlanQty(String fab, String material, List<Date> fabDateList);

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

    /**
     * 批量保存供应商到货计划
     * @param mapList 集合
     */
    void batchSaveArrivalPlan(List<Map> mapList);


    Page<Map> getPageLcmArrivalPlanMaterial(Date startDate, Date endDate, int page, int limit, String searchMaterialGroup,
                                    String searchMaterial, String searchSupplier);

    List<ArrivalPlan> getLcmArrivalPlan(Date startDate, Date endDate, String material, String supplierCode);
}
