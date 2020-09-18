package com.ivo.mrp.service;

import com.ivo.mrp.entity.MonthSettle;

import java.sql.Date;
import java.util.List;

/**
 * 机种需求月结服务接口
 * @author wj
 * @version 1.0
 */
public interface MonthSettleService {

    /**
     * 添加机种的需求月结数据
     * @param fab 厂别
     * @param product 机种
     * @param materialGroup 物料组
     * @param month 结算月份
     * @param settleDate 结算到下月日期
     * @param settleQty 结算数量
     * @param user 用户
     */
    void addMonthSettle(String fab, String product, String materialGroup, String month, Date settleDate, double settleQty,
                        String user);

    /**
     * 删除月结数据
     * @param id ID
     * @param user 用户
     */
    void delMonthSettle(Long id, String user);

    /**
     * 获取某月份的月结数据
     * @param fab 厂别
     * @param month  结算月份
     * @return  List<MonthSettle>
     */
    List<MonthSettle> getMonthSettle(String fab, String month);
}
