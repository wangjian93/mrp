package com.ivo.mrp.service;

/**
 * 料号同步服务接口
 * @author wj
 * @version 1.0
 */
public interface MaterialService {

    /**
     * 同步料号数据 (从81同步)
     */
    void syncMaterial();
}
