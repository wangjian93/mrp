package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.Dps;

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
     * 获取DPS版本的数据
     * @param ver dps版本
     * @return List<Dps>
     */
    List<Dps> getDps(String ver);

    /**
     * 获取DPS版本的数据
     * @param ver dps版本
     * @param product product
     * @return List<Dps>
     */
    List<Dps> getDps(String ver, String product);

    /**
     * 获取DPS的所有版本
     * @return List<String>
     */
    List<String> getDpsVer();

    /**
     * 获取DPS的日期区间日历
     * @param dpsVer dps版本
     * @return List<String>
     */
    List<Date> getDpsCalendarList(String dpsVer);

    /**
     * 导入DPS
     * @param inputStream excel
     * @param fileName 文件名
     * @return dps版本
     */
    String importDps(InputStream inputStream, String fileName);

    /**
     * 获取DPS的厂别
     * @param dpsVer dps版本
     * @return String
     */
    String getPlantByDpsVer(String dpsVer);

    /**
     * 获取dsp版本中的所欲机种
     * @param dpsVer dps版本
     * @return List<String>
     */
    List<String> getProduct(String dpsVer);
}
