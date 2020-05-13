package com.ivo.mrp2.service;

import java.sql.Date;

/**
 * @author wj
 * @version 1.0
 */
public interface InventoryService {

    /**
     * 获取期初库存，（月初7：00系统库存）
     * @param material 料号
     * @param startDate MRP计算的开始日期
     * @return 期初库存
     */
    Double getBeginInventory(String material, Date startDate);
}
