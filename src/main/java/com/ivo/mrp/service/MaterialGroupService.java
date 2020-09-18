package com.ivo.mrp.service;

import com.ivo.mrp.entity.MaterialGroup;
import org.springframework.data.domain.Page;

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

    /**
     * 获取物料组名
     * @param materialGroup 物料组
     * @return 物料组名
     */
    String getMaterialGroupName(String materialGroup);

    /**
     * 分页查询物料组
     * @param page 页数
     * @param limit 分页大小
     * @param search 查询条件
     * @return Page<MaterialGroup>
     */
    Page<MaterialGroup> queryMaterialGroup(int page, int limit, String search);
}
