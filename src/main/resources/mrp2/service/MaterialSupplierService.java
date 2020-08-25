package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.Supplier;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * 材料与供应商数据服务
 * @author wj
 * @version 1.0
 */
public interface MaterialSupplierService {

    /**
     * 分页查询材料与供应商数据
     * @param page 页数
     * @param limit 分页大小
     * @param material 料号
     * @param supplier 供应商ID
     * @return Page<MaterialSupplier>
     */
    Page<Map> getPageMaterialSupplier(int page, int limit, String material, String supplier);

    /**
     * 获取材料的供应商
     * @param material 料号
     * @return List<Supplier>
     */
    List<Supplier> getMaterialSupplier(String material);

    /**
     * 添加一组材料与供应商
     * @param material 料号
     * @param supplierCode 供应商ID
     */
    void add(String material, String supplierCode);

    /**
     * 删除一组材料与供应商
     * @param material 料号
     * @param supplierCode 供应商ID
     */
    void delete(String material, String supplierCode);
}
