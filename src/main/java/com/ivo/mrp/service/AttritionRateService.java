//package com.ivo.mrp.service;
//
//import com.ivo.mrp.entity.AttritionRate;
//
//import java.io.InputStream;
//import java.util.List;
//
///**
// * 供应商损耗率服务接口
// * @author wj
// * @version 1.0
// */
//public interface AttritionRateService {
//
//    /**
//     * excel导入供应商损耗率
//     * @param excel EXCEL
//     * @param fileName EXCEL文件
//     * @return 月份
//     * @throws Exception
//     */
//    void importAttritionRate(InputStream excel, String fileName) throws Exception;
//
//    /**
//     * 保存损耗率
//     * @param attritionRate AttritionRate
//     */
//    void saveAttritionRate(AttritionRate attritionRate);
//
//    /**
//     * 删除损耗率
//     * @param id ID
//     */
//    void delAttritionRate(long id);
//
//    /**
//     * 获取当前有效的损耗率
//     * @param venderCode 供应商Code
//     * @param material 料号
//     * @return AttritionRate
//     */
//    AttritionRate getEffectAttritionRate(String venderCode, String material);
//
//    /**
//     * 条件查询
//     * @param venderCode 供应商Code
//     * @param vender 供应商
//     * @param materialGroup 物料组
//     * @param material 物料
//     * @param effectFlag 是否生效中
//     * @return List<AttritionRate>
//     */
//    List<AttritionRate> getAttritionRate(String venderCode, String vender, String materialGroup, String material, boolean effectFlag);
//}
