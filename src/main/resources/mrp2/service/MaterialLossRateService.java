package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.MaterialLossRate;
import org.springframework.data.domain.Page;

import java.io.InputStream;

/**
 * 材料损耗率
 * @author wj
 * @version 1.0
 */
public interface MaterialLossRateService {

    /**
     * 导入损耗率
     * @param inputStream excel
     * @param fileName 文件名
     */
    void importLossRate(InputStream inputStream, String fileName);

    /**
     * 获取材料的损耗率，没有维护返回0
     * @param material 料号
     * @return 损耗率
     */
    double getMaterialLossRate(String material);

    /**
     * 分页查询损耗率数据
     * @param page 页数
     * @param limit 分页大小
     * @param materialGroup 物料组
     * @param material 料号
     * @return Page<MaterialLossRate>
     */
    Page<MaterialLossRate> getMaterialLossRate(int page, int limit, String materialGroup, String material);


    /**
     * 根据料号获取MaterialLossRate对象
     * @param material 料号
     * @return MaterialLossRate
     */
    MaterialLossRate getMaterialLossRateByMaterial(String material);

    /**
     * 根据物料组获取MaterialLossRate对象
     * @param materialGroup 物料组
     * @return MaterialLossRate
     */
    MaterialLossRate getMaterialLossRateByMaterialGroup(String materialGroup);


    /**
     * 维护料号的损耗率
     * @param material 料号
     * @param lossRate 损耗率
     */
    void saveForMaterial(String material, double lossRate);

    /**
     * 维护物料组的损耗率
     * @param materialGroup 物料组
     * @param lossRate 损耗率
     */
    void saveForMaterialGroup(String materialGroup, double lossRate);

    /**
     * 删除
     * @param id ID
     */
    void delLossRate(long id);

}
