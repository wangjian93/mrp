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

    /**
     * 批量保存
     * @param list  List<Demand>
     */
    void batchSave(List<Demand> list);

    /**
     * 删除
     * @param list List<Demand>
     */
    void delete(List<Demand> list);

    /**
     * 判断dps版本是否已经计算过材料需求
     * @param dpsVer dps版本
     * @return
     */
    boolean isExist(String dpsVer);

    /**
     * 合计料号
     * @param dpsVers DPS版本
     * @return List<String>
     */
    List<Map> sumMaterial(List<String> dpsVers);

    /**
     * 合计材料需求
     * @param dpsVers DPS版本
     * @param material 料号
     * @return List<Map>
     */
    List<Map> sumMaterialDemand(List<String> dpsVers, String material);

    /**
     * 根据DPS版本、日期、料号获取材料需求量
     * @param dpsVer DPS版本
     * @param fabDate 日期
     * @param material 料号
     * @return List<Demand>
     */
    List<Demand> getDemand(String dpsVer, Date fabDate, String material);



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

    /**
     * 按DPS版本获取需求量
     * @param dpsVer DPS版本
     * @return  List<Demand>
     */
    List<Demand> getDemand(String dpsVer);

}
