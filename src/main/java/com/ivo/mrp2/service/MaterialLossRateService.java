package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.MaterialLossRate;
import org.springframework.data.domain.Page;

import java.io.InputStream;
import java.util.List;

/**
 * 材料损耗率
 * @author wj
 * @version 1.0
 */
public interface MaterialLossRateService {

    /**
     * 获取物料的损耗率，当料号没有维护损耗率时将返回物料组的损耗率
     * @param material 料号
     * @return 损耗率
     */
    Double getMaterialLossRate(String material);

    /**
     * 获取材料损耗率
     * @return List<MaterialLossRate>
     */
    List<MaterialLossRate> getMaterialLossRate();

    /**
     * 维护物料的损耗率
     * @param material 料号
     * @param lossRate 损耗率
     */
    void saveMaterialLossRate(String material, double lossRate, String memo);

    /**
     * 维护物料组的损耗率
     * @param materialGroup 物料组
     * @param lossRate 损耗率
     */
    void saveMaterialGroupLossRate(String materialGroup, double lossRate, String memo);

    /**
     * 废止损耗率
     * @param ids 损耗率ID
     */
    void abolishLossRate(long[] ids);

    /**
     * 分页查询物料损耗率
     * @param page 页数
     * @param limit 分页大小
     * @param material 料号
     * @param materialGroup 物料组
     * @param effectFlag 有效性
     * @return Page<MaterialLossRate>
     */
    Page<MaterialLossRate> getPageMaterialLossRate(int page, int limit, String material, String materialGroup, String effectFlag);


    /**
     * 导入损耗率
     * @param inputStream excel
     * @param fileName 文件名
     */
    void importLossRate(InputStream inputStream, String fileName);

}
