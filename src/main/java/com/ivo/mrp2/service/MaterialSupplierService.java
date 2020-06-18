package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.MaterialSupplier;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MaterialSupplierService {

    /**
     * 分页查询材料的供应商
     * @param page 页数
     * @param limit 分页大小
     * @param material 料号
     * @param supplierCode 供应商ID
     * @return Page<MaterialSupplier>
     */
    Page<MaterialSupplier> getPageMaterialSupplier(int page, int limit, String material, String supplierCode);

    /**
     * 同步品名无赖哦组
     * @param materialSupplier MaterialSupplier
     */
    void syncMaterialGroupAndMaterialName(MaterialSupplier materialSupplier);


    /**
     * 获取材料的供应商
     * @param material 料号
     * @return List<MaterialSupplier>
     */
    List<MaterialSupplier> getMaterialSupplier(String material);


    /**
     * 获取供应名称
     * @param supplerCode ID
     * @return String
     */
    String getSupplerName(String supplerCode);
}
