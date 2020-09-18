package com.ivo.mrp.service;

import com.ivo.mrp.entity.LossRate;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;

import java.io.InputStream;
import java.util.List;

/**
 * 材料损耗率维护接口
 * @author wj
 * @version 1.0
 */
public interface LossRateService {

    /**
     * 保存料号的损耗率
     * @param material 料号
     * @param lossRate 损耗率
     * @param user 维护的用户
     */
    void saveMaterialLossRate(String material, double lossRate, String user);

    /**
     * 保存物料组的损耗率
     * @param materialGroup 物料组
     * @param lossRate 损耗率
     * @param user 维护的用户
     */
    void saveMaterialGroupLossRate(String materialGroup, double lossRate, String user);

    /**
     * 删除料号的损耗率
     * @param material 料号
     * @param user 维护的用户
     */
    void delMaterialLossRate(String material, String user);

    /**
     * 删除物料组的损耗率
     * @param materialGroup 物料组
     * @param user 维护的用户
     */
    void delMaterialGroupLossRate(String materialGroup, String user);

    /**
     * 获取材料的损耗率
     * @param material 料号
     * @return 损耗率
     */
    double getLossRate(String material);

    /**
     * 分页查询材料的损耗率
     * @param page 页数
     * @param limit 分页大小
     * @param searchGroup 查询物料组
     * @param searchMaterial 查询料号
     * @return Page<LossRate>
     */
    Page<LossRate> queryLossRate(int page, int limit, String searchGroup, String searchMaterial);

    /**
     * 查询材料的损耗率
     * @param searchGroup 查询物料组
     * @param searchMaterial 查询料号
     * @return List<LossRate>
     */
    List<LossRate> queryLossRate(String searchGroup, String searchMaterial);

    /**
     * excel导入损耗率数据
     * @param inputStream excel
     * @param fileName 文件名
     */
    void importExcel(InputStream inputStream, String fileName);

    /**
     * excel导出损耗率全部数据
     */
    Workbook exportExcel();

    /**
     * excel导出损耗率数据
     * @param searchGroup 查询条件物料组
     * @param searchMaterial 查询条件料号
     */
    Workbook exportExcel(String searchGroup, String searchMaterial);

    /**
     * 下载损耗率Excel模板
     * @return Workbook
     */
    Workbook downloadExcel();
}
