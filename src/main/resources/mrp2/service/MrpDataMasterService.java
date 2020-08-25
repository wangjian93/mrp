package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.MrpDataMaster;
import com.ivo.mrp2.entity.MrpVer;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;

/**
 * MRP的主数据服务接口
 * @author wj
 * @version 1.0
 */
public interface MrpDataMasterService {

    /**
     * MRP中料号的信息
     * @param mrpVer MRP版本
     * @param material 料号
     * @return MrpDataMaster
     */
    MrpDataMaster getMrpMaterial(String mrpVer, String material);

    List<String> getMaterial(String mrpVer);
    /**
     * 获取MRP中的料号
     * @param mrpVer MRP版本
     * @return  List<String>
     */
    List<String> getMaterial(String mrpVer, String searchMaterial);

    /**
     * 获取MRP中的物料组
     * @param mrpVer MRP版本
     * @return  List<String>
     */
    List<String> getMaterialGroup(String mrpVer, String searchMaterialGroup);

    /**
     * 获取MRP中的机种
     * @param mrpVer MRP版本
     * @return  List<String>
     */
    List<String> getProduct(String mrpVer, String searchProduct);

    List<String> getSupplier(String mrpVer, String searchSupplier);

    /**
     * 获取MRP中的供应商
     * @param mrpVer MRP版本
     * @return  List<String>
     */
    List<String> getSupplier(String mrpVer);

    /**
     * 保存
     * @param mrpDataMaster MrpMaterial对象
     */
    void saveMrpMaterial(MrpDataMaster mrpDataMaster);


    /**
     * 查询MrpMaterial
     * @param mrpVer MRP版本
     * @return  List<String>
     */
    List<MrpDataMaster> getMrpMaterial(String mrpVer);

    /**
     * 分页查询MRP中的料号
     * @param page 页数
     * @param limit 分页大小
     * @param mrpVer MRP版本
     * @param material 料号
     * @param product 机种
     * @param materialGroup 物料组
     * @return Page
     */
    Page<MrpDataMaster> getPageMrpData(int page, int limit, String mrpVer, String product, String materialGroup, String material, String supplier);

    Page<String> getPageMrpData(int page, int limit, List<String> mrpVer, String product, String materialGroup, String material);
}
