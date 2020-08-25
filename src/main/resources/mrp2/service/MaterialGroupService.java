package com.ivo.mrp2.service;

import java.util.List;

/**
 * 物料组服务
 * @author wj
 * @version 1.0
 */
public interface MaterialGroupService {

    /**
     * 获取物料组名
     * @param materialGroup 物料组
     * @return 物料组名
     */
    String getMaterialGroupName(String materialGroup);

    /**
     * 同步物料组
     */
    void syncMaterialGroup();

    /**
     * 查询物料组
     * @param materialGroupSearch 模糊查询物料组
     * @param limit 数据最大条数
     * @return List<String>
     */
    List<String> searchMaterialGroup(String materialGroupSearch, int limit);
}
