package com.ivo.mrp.service;

import com.ivo.mrp.entity.direct.ary.DemandAry;
import com.ivo.mrp.entity.direct.ary.DemandAryOc;
import com.ivo.mrp.entity.direct.cell.DemandCell;
import com.ivo.mrp.entity.direct.lcm.DemandLcm;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * 需求量计算服务接口
 * @author wj
 * @version 1.0
 */
public interface DemandService {

    /**
     * 保存LCM的需求
     * @param demandLcmList 集合
     */
    void saveDemandLcm(List<DemandLcm> demandLcmList);

    /**
     * 保存ARY的需求
     * @param demandAryList 集合
     */
    void saveDemandAry(List<DemandAry> demandAryList);

    /**
     * 保存ARY OC需求
     * @param demandAryOcList 集合
     */
    void saveDemandAryOc(List<DemandAryOc> demandAryOcList);

    /**
     * 保存CELL的需求
     * @param demandCellList 集合
     */
    void saveDemandCell(List<DemandCell> demandCellList);

    /**
     * 统计出LCM MRP版本的所有料号
     * @param ver MRP版本
     * @return List<String>
     */
    List<String> getDemandMaterialLcm(String ver);

    /**
     * 统计出ARY MRP版本的所有料号
     * @param ver MRP版本
     * @return List<String>
     */
    List<String> getDemandMaterialAry(String ver);


    /**
     * 统计出CELL MRP版本的所有料号
     * @param ver MRP版本
     * @return List<String>
     */
    List<String> getDemandMaterialCell(String ver);

    /**
     * 获取LCM MRP材料的每天需求量
     * @param ver MRP版本
     * @param material 料号
     * @return List<Map<Date, Double>>
     */
    Map<Date, Double> getDemandQtyLcm(String ver, String material);

    /**
     * 获取ART MRP材料的每天需求量
     * @param ver MRP版本
     * @param material 料号
     * @return List<Map<Date, Double>>
     */
    Map<Date, Double> getDemandQtyAry(String ver, String material);

    /**
     * 获取CELL MRP材料的每天需求量
     * @param ver MRP版本
     * @param material 料号
     * @return List<Map<Date, Double>>
     */
    Map<Date, Double> getDemandQtyCell(String ver, String material);

    /**
     * 获取LCM MRP材料的需求量
     * @param ver MRP版本
     * @param material 料号
     * @param fabDate 日期
     * @return double
     */
    double getDemandQtyLcm(String ver, String material, Date fabDate);

    /**
     * 获取ARY MRP材料的需求量
     * @param ver MRP版本
     * @param material 料号
     * @param fabDate 日期
     * @return double
     */
    double getDemandQtyAry(String ver, String material, Date fabDate);

    /**
     * 获取CELL MRP材料的需求量
     * @param ver MRP版本
     * @param material 料号
     * @param fabDate 日期
     * @return double
     */
    double getDemandQtyCell(String ver, String material, Date fabDate);

    /**
     * 获取Ary MRP材料的需求量明细
     * @param ver MRP版本
     * @param material 料号
     * @param fabDate 日期
     * @return List<DemandAry>
     */
    List<DemandAry> getDemandAry(String ver, String material, Date fabDate);

    /**
     * 获取Ary OC MRP材料的需求量明细
     * @param ver MRP版本
     * @param material 料号
     * @param fabDate 日期
     * @return List<DemandAry>
     */
    List<DemandAryOc> getDemandAryOc(String ver, String material, Date fabDate);

    /**
     * 获取CELL MRP材料的需求量明细
     * @param ver MRP版本
     * @param material 料号
     * @param fabDate 日期
     * @return List<DemandAry>
     */
    List<DemandCell> getDemandCell(String ver, String material, Date fabDate);

    /**
     * 获取LCM MRP材料的需求量明细
     * @param ver MRP版本
     * @param material 料号
     * @param fabDate 日期
     * @return List<DemandAry>
     */
    List<DemandLcm> getDemandLcm(String ver, String material, Date fabDate);


    /**
     * 获取LCM 料号的需求机种
     * @param ver MRP版本
     * @param material 料号
     * @return List<String>
     */
    List<String> getDemandProductLcm(String ver, String material);

    /**
     * 统计出ARY 料号的需求机种
     * @param ver MRP版本
     * @param material 料号
     * @return List<String>
     */
    List<String> getDemandProductAry(String ver, String material);


    /**
     * 统计出CELL 料号的需求机种
     * @param ver MRP版本
     * @param material 料号
     * @return List<String>
     */
    List<String> getDemandProductCell(String ver, String material);


    void deleteDemand(String ver);
}
