//package com.ivo.mrp.service;
//
//import com.ivo.mrp.entity.VenderArrivalPlan;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.InputStream;
//import java.util.List;
//
///**
// * 供应商到货计划服务接口
// * @author wj
// * @version 1.0
// */
//public interface VenderArrivalPlanService {
//
//    /**
//     * 动态条件查询，根据月份，供应商，料号获取到货计划
//     * @param month 月份 例'2020-02'
//     * @param vender 供应商
//     * @param material 料号
//     * @return
//     */
//    List<VenderArrivalPlan> getVenderArrivalPlan(String month, String vender, String material);
//
//
//    /**
//     * 按月excel导入供应商到货计划
//     * @param excel EXCEL
//     * @param fileName EXCEL文件文
//     * @return 月份
//     * @throws Exception
//     */
//    String importVenderArrivalPlanByMonth(InputStream excel, String fileName) throws Exception;
//
//    /**
//     * 导出供应商的到货计划
//     * @param month 月份
//     * @param vender 供应商
//     * @param material 料号
//     * @return XSSFWorkbook
//     */
//    XSSFWorkbook exportVenderArrivalPlan(String month, String vender, String material);
//}
