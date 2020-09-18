package com.ivo.mrp.service;

import com.ivo.mrp.entity.Project;
import org.springframework.data.domain.Page;

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

    /**
     * 获取机种
     * @param project 机种名
     * @return Project
     */
    Project getProject(String project);

    /**
     * 分页获取机种信息
     * @param page 页数
     * @param limit 分页大小
     * @param search 查询条件
     * @return Page<Project>
     */
    Page<Project> queryProject(int page, int limit, String search);
}
