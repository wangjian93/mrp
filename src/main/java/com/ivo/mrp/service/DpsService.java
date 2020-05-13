//package com.ivo.mrp.service;
//
//import com.ivo.mrp.entity.Dps;
//
//import java.io.InputStream;
//import java.util.Date;
//import java.util.List;
//
///**
// * DPS & MPS服务接口
// * @author wj
// * @version 1.0
// */
//public interface DpsService {
//
//    /**
//     * 根据版本获取DPS
//     * @param ver 版本
//     * @return List<RestDps>
//     */
//    List<Dps> getDps(String ver);
//
//    /**
//     * @param ver 版本
//     * @param fab 厂
//     * @param product 产品
//     * @param fabDate 日期
//     * @return
//     */
//    Dps getDps(String ver, String fab, String product, Date fabDate);
//
//    /**
//     * 模糊搜索DPS版本
//     * @return List<String>
//     */
//    List<String> getDpsVer();
//
//    /**
//     * 按版本excel上传DPS
//     * @param excel Excel
//     * @param fileName 文件名
//     * @return dps版本
//     */
//    String importDpsByVer(InputStream excel, String fileName);
//
//    /**
//     * 按版本同步DPS
//     * @param ver dps版本
//     */
//    void syncDpsByVer(String ver);
//
//    /**
//     * 获取DPS版本的开始和结束日期
//     * @param ver dps版本
//     * @return Date[]
//     */
//    Date[] getStartAndEndDate(String ver);
//}
