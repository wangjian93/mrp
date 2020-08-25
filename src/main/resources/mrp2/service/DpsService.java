package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.Dps;
import com.ivo.mrp2.entity.DpsData;
import com.ivo.mrp2.entity.DpsVer;
import org.springframework.data.domain.Page;

import java.io.InputStream;
import java.sql.Date;
import java.util.List;

/**
 * DPS服务接口
 * @author wj
 * @version 1.0
 */
public interface DpsService {

    /**
     * 同步
     */
    void sync();

    /**
     * 同步LCM DPS
     */
    void syncLcmDps(String ver);

    /**
     * 同步CELL DPS
     */
    void syncCellDps(String ver);

    /**
     * 同步CELL DPS
     */
    void syncArrayDps(String ver);


    /**
     * 导入DPS
     * @param inputStream 输入流
     * @param fileName 文件名
     * @return DPS版本
     */
    String importDps(InputStream inputStream, String fileName);

    /**
     * 生成新的DPS版本
     * @return  DPS版本
     */
    String generateDpsVer();

    /**
     * 导出Dps
     * @param ver dps版本
     */
    void exportDps(String ver);

    /**
     * 分页查询DPS版本
     * @param page 页数
     * @param limit 分页大小
     * @return Page<DpsVer>
     */
    Page<DpsVer> getDpsVer(int page, int limit);

    /**
     * 获取某一版本的DPS
     * @param ver DPS版本
     * @return List<DpsData>
     */
    List<DpsData> getDpsData(String ver);



    /**
     * 获取DPS的日期区间日历
     * @param ver dps版本
     * @return List<String>
     */
    List<Date> getDpsCalendarList(String ver);

    /**
     * 调整DPS数量
     * @param ver 版本
     * @param product 机种
     * @param fabDate 日期
     * @param resizeQty 调整数量（加量/减量）
     */
    void resizeQty(String ver, String product, Date fabDate, double resizeQty);

    /**
     * 获取DpsData对象
     * @param ver 版本
     * @param product 机种
     * @param fabDate 日期
     * @return DpsData
     */
    DpsData getDpsDate(String ver, String product, Date fabDate);

    /**
     * 获取DPS的机种数据
     * @param ver dps版本
     * @param product product
     * @return List<Dps>
     */
    List<DpsData> getDpsData(String ver, String product);


    /**
     * 取消DPS调整
     * @param ver 版本
     * @param product 机种
     * @param fabDate 日期
     */
    void cancelResize(String ver, String product, Date fabDate);

    /**
     * 获取DPS中所有机种
     * @param dpsVer dps版本
     * @return List<String>
     */
    List<String> getProduct(String dpsVer);







    /**
     * 从DPS数据库同步DPS
     */
    void syncDps();

    /**
     * 判断DPS的版本是否存在
     * @param ver dps版本
     * @return boolean
     */
    boolean isExistVer(String ver);

    /**
     * 获取DPS的开始和结束日期区间
     * @param ver dps版本
     * @return {开始日期, 结束日期}
     */
    Date[] getDpsDateRange(String ver);


    /**
     * 获取DPS的所有版本
     * @return List<String>
     */
    List<String> getDpsVer();

    /**
     * 判断DPS的厂别
     * @param dpsVer dps版本
     * @return String
     */
    String getPlantByDpsVer(String dpsVer);

    List<Dps> getDps(String ver, String product);




    /**
     * 根据版本获取DPS版本对象
     * @param ver 版本
     * @return DpsVer
     */
    DpsVer getDpsVer(String ver);
}
