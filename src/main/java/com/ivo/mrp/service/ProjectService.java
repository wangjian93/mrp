package com.ivo.mrp.service;

/**
 * 机种同步服务接口
 * @author wj
 * @version 1.0
 */
public interface ProjectService {

    /**
     * 同步机种数据 (从81同步)
     */
    void syncProject();
}
