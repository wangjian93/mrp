package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.Dps;
import com.ivo.mrp2.entity.MrpVer;

import java.sql.Date;
import java.util.List;

/**
 * MRP计算服务接口
 * @author wj
 * @version 1.0
 */
public interface MrpService {

    /**
     * 获取MRP版本对象
     * @param mrpVer mrp版本
     * @return MrpVer
     */
    MrpVer getMrpVer(String mrpVer);

    /**
     * 获取MRP的所有版本字符串
     * @return List<String>
     */
    List<String> getMrpVerStr();

    /**
     * 获取所有的MRP版本
     * @return List<String>
     */
    List<MrpVer> getMrpVer();

    /**
     * 选择DPS，生成MRP版本
     * @param dpsVer dps版本
     * @return mrp版本
     */
    void generateMrp(String dpsVer);

    /**
     * 确定MRP版本的日期区间
     * @param mrpVer mrp版本
     */
    void determineDateRange(String mrpVer);

    /**
     * 展开MRP的DPS
     * @param mrpVer 版本
     */
    void expandDps(String mrpVer);

    /**
     * 展开机种的DPS
     * @param mrpVer mrp版本
     * @param product 机种
     */
    void expandProduct(String mrpVer, String product);


    /**
     * 计算材料的需求量
     * @param mrpVer mrp版本
     */
    void computeDemand(String mrpVer);

    /**
     * 计算材料的损耗数量
     * @param mrpVer mrp版本
     */
    void computeLoss(String mrpVer);

    /**
     * 计算材料的到货数量
     * @param mrpVer mrp版本
     */
    void computeArrival(String mrpVer);


    /**
     * 计算材料的结余数量
     * @param mrpVer mrp版本
     */
    void computeBalance(String mrpVer);

    /**
     * 获取MRP版本的日历
     * @param mrpVer mrp版本
     * @return List<Date>
     */
    List<Date> getMrpCalendarList(String mrpVer);







}
