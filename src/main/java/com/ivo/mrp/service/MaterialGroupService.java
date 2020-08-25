package com.ivo.mrp.service;

/**
 * 物料组同步服务接口
 * @author wj
 * @version 1.0
 */
public interface MaterialGroupService {

    /**
     * 同步物料组数据 (从81同步)
     */
    void syncMaterialGroup();
}
