package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.MaterialSupplier;
import org.springframework.data.domain.Page;

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

}
