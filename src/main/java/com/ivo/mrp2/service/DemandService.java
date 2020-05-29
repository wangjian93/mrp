package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.Demand;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * 材料的每日需求量及损耗量计算
 * @author wj
 * @version 1.0
 */
public interface DemandService {

//    /**
//     * bom展开后的材料需求量汇总
//     * @param dpsVer dps版本
//     * @return 料号集合
//     */
//    List<Map> summaryMaterial(String dpsVer);

    /**
     * 批量保存
     * @param list  List<Demand>
     */
    void batchSave(List<Demand> list);

//    /**
//     * 获取MRP版本中的材料在某天的需求量和损耗量
//     * @param mrpVer mrp版本
//     * @param material 料号
//     * @param fabDate 日期
//     * @return Demand
//     */
//    Demand getMaterialDailyDemandLoss(String mrpVer, String material, Date fabDate);
//
//    /**
//     * 获取材料的需求量和损耗量
//     * @param mrpVer mrp版本
//     * @return
//     */
//    List<Demand> getMaterialDailyDemandLoss(String mrpVer);

//    /**
//     * 获取材料的需求量和损耗量
//     * @param mrpVer mrp版本
//     * @param material 料号
//     * @return
//     */
//    List<Demand> getMaterialDailyDemandLoss(String mrpVer, String material);

//    /**
//     * 根据DPS版本获取材料需求量
//     * @param dpsVer dps版本
//     * @return
//     */
//    List<Demand> getDemand(String dpsVer);

    /**
     * 根据DPS版本、日期、料号获取材料需求量
     * @param dpsVer DPS版本
     * @param fabDate 日期
     * @param material 料号
     * @return
     */
    List<Demand> getDemand(String dpsVer, Date fabDate, String material);

//    /**
//     * 获取机种展开BOM后的材料需求量
//     * @param dpsVer DPS版本
//     * @param product 机种
//     * @return
//     */
//    List<Demand> getDemand(String dpsVer, String product);


    /**
     * 获取DPS展开后的料号
     * @param dpsVer DPS版本
     * @return  List<String>
     */
    List<String> getMaterial(String dpsVer);

    /**
     * 获取DPS展开后的材料需求量
     * @param dpsVer DPS版本
     * @param material 料号
     * @return List<Demand>
     */
    List<Demand> getDemandByMaterial(String dpsVer, String material);
}
