package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.Material;

import java.util.List;

/**
 * 物料服务
 * @author wj
 * @version 1.0
 */
public interface MaterialService {

    /**
     * 获取料号
     * @param material 料号
     * @return Material
     */
    Material getMaterial(String material);

    /**
     * 获取物料名
     * @param material 料号
     * @return 物料名
     */
    String getMaterialName(String material);

    /**
     * 获取料号的物料组
     * @param material 料号
     * @return 物料组
     */
    String getMaterialGroup(String material);

    /**
     * 同步物料
     */
    void syncMaterial();

    /**
     * 查询料号
     * @param materialSearch 模糊查询料号
     * @param limit 数据最大条数
     * @return List<String>
     */
    List<String> searchMaterial(String materialSearch, int limit);

    /**
     * 获取物料组的所有料号
     * @param materialGroup 物料组
     * @return List<String>
     */
    List<String> getMaterialByGroup(String materialGroup);
}
