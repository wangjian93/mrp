package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.Material;
import com.ivo.mrp2.entity.MaterialGroup;
import com.ivo.mrp2.entity.MaterialSubstitute;

/**
 * 缓存技术使用
 * @author wj
 * @version 1.0
 */
public interface CacheService {

    /**
     * 系统缓存物料信息
     * @param material 料号
     * @return Material
     */
    Material getMaterial(String material);

    /**
     * 系统缓存物料组信息
     * @param materialGroup 物料组
     * @return MaterialGroup
     */
    MaterialGroup getMaterialGroup(String materialGroup);

    /**
     * 系统缓存替代料信息
     * @param plant 厂别
     * @param product 机种
     * @param material 料号
     * @return
     */
    MaterialSubstitute getMaterialSubstitute(String plant, String product, String material);
}
