package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.Bom;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Bom服务接口
 * @author wj
 * @version 1.0
 */
public interface BomService {

    /**
     * 从Auto PR同步LCM1/LCM2/CELL的BOM
     */
    void syncBom();

    /**
     * 料号获取物料组
     * @param material 料号
     * @return 物料组
     */
    String getMaterialGroup(String material);

    /**
     * 获取材料品名
     * @param material 料号
     * @return 品名
     */
    String getMaterialName(String material);

    /**
     * 展开机种的使用材料
     * @param product 机种
     * @param plant 厂别
     * @return List<Bom>
     */
    List<Bom> getBomByProductAndPlant(String product, String plant);

    /**
     * 分页查询BOM清单
     * @param page 页码
     * @param limit 分页大小
     * @param product 产品
     * @param material 料号
     * @param plant 厂别
     * @return Page<Bom>
     */
    Page<Bom> pageQueryBom(int page, int limit,  String product, String material, String plant);
}
