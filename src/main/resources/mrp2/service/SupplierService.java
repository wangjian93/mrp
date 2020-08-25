package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.Supplier;
import org.springframework.data.domain.Page;


/**
 * 供应商服务
 * @author wj
 * @version 1.0
 */
public interface SupplierService {

    /**
     * 修改供应商简称
     * @param id 供应商ID
     * @param sName 简称
     */
    void updateSName(String id, String sName);


    /**
     * 获取供应商
     * @param supplierCode 供应商ID
     * @return Supplier
     */
    Supplier getSupplier(String supplierCode);

    /**
     * 查询供应商
     * @param supplierSearch 供应商模糊查询
     * @param limit 最大条数
     * @return
     */
    Page<Supplier> searchSupplier(String supplierSearch, int limit);

    /**
     * 分页查询供应商
     * @param page 页数
     * @param limit 分页大小
     * @param supplier 供应商
     * @return Page<Supplier>
     */
    Page<Supplier> getPageSupplier(int page, int limit, String supplier);
}
