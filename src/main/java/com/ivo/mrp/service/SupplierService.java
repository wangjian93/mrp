package com.ivo.mrp.service;

import com.ivo.mrp.entity.Supplier;
import com.ivo.mrp.entity.direct.SupplierMaterial;
import com.ivo.mrp.entity.packaging.SupplierPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface SupplierService {

    /**
     * 根据ID获取供应商
     * @param supplierCode 供应商ID
     * @return Supplier
     */
    Supplier getSupplier(String supplierCode);

    /**
     * 获取料号与供应商
     * @param supplierCode 供应商ID
     * @param material 料号
     * @return SupplierMaterial
     */
    SupplierMaterial getSupplierMaterial(String supplierCode, String material);



    /**
     * 获取料号的供应商
     * @param material 料号
     * @return List<Supplier>
     */
    List<Supplier> getSupplierByMaterial(String material);

    /**
     * 添加料号的供应商
     * @param supplierCode 供应商ID
     * @param material 料号
     * @param user 用户
     */
    void addSupplierMaterial(String supplierCode, String material,  String user);

    /**
     * 删除料号的供应商
     * @param supplierCode 供应商ID
     * @param material 料号
     * @param user 用户
     */
    void delSupplierMaterial(String supplierCode, String material, String user);

    /**
     * excel导入材料与供应商数据
     * @param inputStream excel
     * @param fileName 文件名
     */
    void importSupplierMaterial(InputStream inputStream, String fileName);






    /**
     * 更新供应商信息
     * @param supplierCode 供应商ID
     * @param supplierName 供应商名称
     * @param supplierSname 简称
     * @param user 用户
     */
    void updateSupplier(String supplierCode, String supplierName, String supplierSname, String user);

    /**
     * 添加供应商
     * @param supplierCode 供应商ID
     * @param supplierName 供应商名称
     * @param supplierSname 简称
     * @param user 用户
     */
    void addSupplier(String supplierCode, String supplierName, String supplierSname, String user);

    /**
     * 分页查询供应商
     * @param page 页数
     * @param limit 分页大小
     * @param search 查询条件
     * @return Page<Supplier>
     */
    Page<Supplier> querySupplier(int page, int limit, String search);

    /**
     * Excel导出供应商数据
     * @return Workbook
     */
    Workbook exportExcel();

    /**
     * Excel导入供应商数据
     * @param inputStream excel文件
     * @param fileName 文件名
     */
    void importExcel(InputStream inputStream, String fileName);

    /**
     * 分页查询主材的供应商
     * @param page 页数
     * @param limit 分页大小
     * @param searchMaterial 料号查询
     * @param searchSupplier 供应商查询
     * @return Page<SupplierMaterial>
     */
    Page<Map> querySupplierMaterial(int page, int limit, String searchMaterial, String searchSupplier);

    /**
     * 分页查询包材的供应商
     * @param page 页数
     * @param limit 分页大小
     * @param month 月份
     * @param searchProduct 机种查询
     * @return Page<SupplierPackage>
     */
    Page<SupplierPackage> querySupplierPackage(int page, int limit, String month, String searchProduct);
}
