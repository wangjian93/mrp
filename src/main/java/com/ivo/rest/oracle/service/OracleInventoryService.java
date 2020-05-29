package com.ivo.rest.oracle.service;

import java.sql.Date;

/**
 * 获取材料的库存量
 * @author wj
 * @version 1.0
 */
public interface OracleInventoryService {

    /**
     * 获取良品仓的材料库存
     * @param plant 厂别
     * @param material 料号
     * @param fabDate 日期
     * @return 期初库存
     */
    double getGoodInventory(String plant, String material, Date fabDate);


    /**
     * 获取呆滞料库存
     * @param plant 厂别
     * @param material 料号
     * @param fabDate MRP计算的开始日期
     * @return 期初库存
     */
    double getDullInventory(String plant, String material, Date fabDate);

}
