package com.ivo.mrp.service;

/**
 * 机种的切片数服务接口
 * @author wj
 * @version 1.0
 */
public interface CutService {

    /**
     * 获取机种的切片数
     * @param project 机种
     * @return Double切片数
     */
    Double getProjectCut(String project);
}
