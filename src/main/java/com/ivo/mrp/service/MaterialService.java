package com.ivo.mrp.service;

import com.ivo.mrp.entity.Material;
import org.springframework.data.domain.Page;

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

    /**
     * 获取料号的物料组
     * @param material 料号
     * @return 物料组
     */
    String getMaterialGroup(String material);

    /**
     * 获取料号的物料名
     * @param material 料号
     * @return 物料名
     */
    String getMaterialName(String material);

    /**
     * 分页查询料号信息
     * @param page 页数
     * @param limit 分页大小
     * @param search 查询条件
     * @return Page<Material>
     */
    Page<Material> queryMaterial(int page, int limit, String search);

    /**
     * 分页查询料号信息
     * @param page 页数
     * @param limit 分页大小
     * @param materialGroup 物料组
     * @param search 查询条件
     * @return Page<Material>
     */
    Page<Material> queryMaterial(int page, int limit, String materialGroup, String search);
}
