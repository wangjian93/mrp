package com.ivo.rest;

import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * 接口访问服务
 * @author wj
 * @version 1.0
 */
public interface RestService {

    /**
     * 从81数据库的表MM_O_MaterialGroup同步数据
     * @return List<Map>
     */
    List<Map> getMaterialGroup();

    /**
     * 从81数据库的表MM_O_Material同步数据
     * @return List<Map>
     */
    List<Map> getMaterial();

    /**
     * 从81数据库的表BG_O_Project同步数据
     * @return List<Map>
     */
    List<Map> getProject();

    /**
     * 从81数据库MM_V_BOM_M1获取LCM1的BOM
     * @return  List<Map>
     */
    List<Map> getBomLcm1();

    /**
     * 从81数据库MM_V_BOM_M2获取LCM2的BOM
     * @return List<Map>
     */
    List<Map> getBomLcm2();

    /**
     * 从MPS数据库同步Ary的BOM的15料号
     * @return List<Map>
     */
    List<Map> getBomAry();

    /**
     * ARY的15料号查询51料号
     * @param aryMtr ARY的15料号
     * @return List<Map>
     */
    List<Map> getMaterialByAryMtrl(String aryMtr);

    /**
     * 获取MPS中的CELL BOM成品料号
     * @return List<Map>
     */
    List<Map> getBomCell();

    /**
     * 从Oracle数据库V_BOM_CELL_C获取成品料号的材料
     * @param cellMtrl 成品料号
     * @return
     */
    List<Map> getCellMtrl(String cellMtrl);

    /**
     * 从81数据库mm_v_bom_m1replace获取LCM1的替代料关系
     * @return List<Map>
     */
    List<Map> getMaterialSubstituteLcm1();

    /**
     * 从81数据库mm_v_bom_m2replace获取LCM2的替代料关系
     * @return List<Map>
     */
    List<Map> getMaterialSubstituteLcm2();

    /**
     * 从81数据库MM_O_CELLBOM_REPLACE获取CELL的替代料关系
     * @return List<Map>
     */
    List<Map> getMaterialSubstituteCell();

    /**
     * 从2.115 DPS数据库获取LCM DPS数据
     * @param ver 版本
     * @return List<Map>
     */
    List<Map> getDpsLcm(String ver);

    /**
     * 从2.115 DPS数据库获取LCM DPS的版本
     * @return List<String>
     */
    List<String> getDpsLcmVer();

    /**
     * 从2.75 DPS数据库获取CELL的DPS数据 (Cell Input)
     * @param ver dps版本
     * @return List<Map>
     */
    List<Map> getDpsCell(String ver);

    /**
     * 从2.75 DPS数据库获获取ARY的DPS数据 (Array Input)
     * @param ver dps版本
     * @return List<Map>
     */
    List<Map> getDpsAry(String ver);

    /**
     * 从2.75 DPS数据库获取CELL/ARY DPS PC上传的版本
     * @return List<String>
     */
    List<String> getDpsCellAryVer();

    /**
     * 从2.75 DPS数据库获取ARY OC材料的DPS数据
     * @param ver dps版本
     * @return List<Map>
     */
    List<Map> getDpsAryOc(String ver);

    /**
     * 从2.75 DPS数据库获取CELL包材的DPS数据
     * @param ver dps版本
     * @param productList 机种
     * @return List<Map>
     */
    List<Map> getDpsPackage(String ver, List<String> productList);



    /**
     * 从Oracle数据库获取良品仓的材料库存
     * @param plant 厂别
     * @param material 料号
     * @param fabDate 日期
     * @return 期初库存
     */
    double getGoodInventory(String plant, String material, Date fabDate);

    /**
     * 从Oracle数据库获取呆滞料库存
     * @param plant 厂别
     * @param material 料号
     * @param fabDate MRP计算的开始日期
     * @return 期初库存
     */
    double getDullInventory(String plant, String material, Date fabDate);

    /**
     * 从Oracle数据库批量获取良品仓的材料库存
     * @param materialList 料号集合
     * @param fabDate 日期
     * @param plant 厂别
     * @return List<Map>
     */
    List<Map> getGoodInventory(String plant,  List<String> materialList, Date fabDate);

    /**
     * 从Oracle数据库批量获取呆滞料库存
     * @param materialList 料号集合
     * @param fabDate 日期
     * @param plant 厂别
     * @return List<Map>
     */
    List<Map> getDullInventory(String plant, List<String> materialList, Date fabDate);

    /**
     * 从81数据库获取料号供应商数据
     * @return List<Map>
     */
    List<Map> getSupplierMaterial();

    /**
     * 从81数据库获取供应商的实际到货量
     * @return List<Map>
     */
    List<Map> getActualArrivalQty(@Param("fabDate") Date fabDate);

    List<Map> getAryMps(@Param("ver") String ver);

    List<String> getAryMpsVer();

    List<Map> getCellMps(@Param("ver") String ver);

    List<String> getCellMpsVer();



    /**
     * 获取MPS的DateOfInsert作版本
     * @return List<String>
     */
    List<String> getMpsDateOfInsertForVersion();

    /**
     * 获取ARY MPS的DateOfInsert版本数据
     * @param dateOfInsert
     * @return  List<Map>
     */
    List<Map> getAryMpsData(String dateOfInsert);

    /**
     * 获取CELL MPS的DateOfInsert版本数据
     * @param dateOfInsert
     * @return  List<Map>
     */
    List<Map> getCellMpsData(String dateOfInsert);



    /**
     * 获取LCM1的包材BOM
     * @return  List<Map>
     */
    List<Map> getBomPackageLcm1();

    /**
     * 获取LCM2的包材BOM
     * @return List<Map>
     */
    List<Map> getBomPackageLcm2();
}
