package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.MonthSettlement;

import java.sql.Date;
import java.util.List;

/**
 * 月结服务
 * @author wj
 * @version 1.0
 */
public interface MonthSettlementService {

    /**
     * 获取某月份的月结数据
     * @param plant 厂别
     * @param month 月份
     * @return List<MonthSettlement>
     */
    List<MonthSettlement> getMonthSettlement(String plant, String month);

    /**
     * 保存数据
     * @param plant 厂别
     * @param month 月份
     * @param product 机种
     * @param materialGroup 物料组
     * @param qty 数量
     * @param settlementDate 下月日期
     */
    void save(String plant, String month, String product, String materialGroup, double qty, Date settlementDate);

    /**
     * 删除数据
     * @param list 集合
     */
    void delete(List<MonthSettlement> list);
}
