package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.MrpMaterial;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * MRP的料号服务接口
 * @author wj
 * @version 1.0
 */
public interface MrpMaterialService {

    /**
     * MRP中料号的信息
     * @param mrpVer MRP版本
     * @param material 料号
     * @return MrpMaterial
     */
    MrpMaterial getMrpMaterial(String mrpVer, String material);

    /**
     * 获取MRP中的料号
     * @param mrpVer MRP版本
     * @return  List<String>
     */
    List<String> getMaterial(String mrpVer);

    /**
     * 获取MRP中的物料组
     * @param mrpVer MRP版本
     * @return  List<String>
     */
    List<String> getMaterialGroup(String mrpVer);

    /**
     * 保存
     * @param mrpMaterial MrpMaterial对象
     */
    void saveMrpMaterial(MrpMaterial mrpMaterial);


    /**
     * 分页查询
     * @param page 页数
     * @param limit 分页大小
     * @param mrpVer MRP版本
     * @param material 料号
     * @param product 机种
     * @param materialGroup 物料组
     * @return Page
     */
    Page<MrpMaterial> getPageMrpData(int page, int limit, String mrpVer, String product, String materialGroup, String material);

    /**
     * 查询MrpMaterial
     * @param mrpVer MRP版本
     * @return  List<String>
     */
    List<MrpMaterial> getMrpMaterial(String mrpVer);
}
