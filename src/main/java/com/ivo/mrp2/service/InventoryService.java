package com.ivo.mrp2.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface InventoryService {

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


    /**
     * 集中获取材料库存
     * @param materialList 料号集合
     * @param fabDate 日期
     * @param plant 厂别
     * @return List<Map>
     */
    List<Map> getGoodInventory(List<String> materialList, Date fabDate, String plant);

    /**
     * 集中获取呆滞料库存
     * @param materialList 料号集合
     * @param fabDate 日期
     * @param plant 厂别
     * @return List<Map>
     */
    List<Map> getDullInventory(List<String> materialList, Date fabDate, String plant);
}
