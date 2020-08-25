package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.Bom;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

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
     * 获取BOM全部数据
     * @return List<Bom>
     */
    List<Bom> getAllBom();

    /**
     * 获取机种的BOM材料清单
     * @param plant 厂别
     * @param product  机种
     * @return List<Bom>
     */
    List<Bom> getProductBom(String plant, String product);

    /**
     * 获取机种某物料组的BOM材料清单
     * @param plant 厂别
     * @param product 机种
     * @param materialGroup 物料组
     * @return List<Bom>
     */
    List<Bom> getMaterialGroupBom(String plant, String product, String materialGroup);

    /**
     * 获取BOM
     * @param plant 厂别
     * @param product 机种
     * @param material 料号
     * @return Bom
     */
    Bom getBom(String plant, String product, String material, String material_);

    /**
     * 保存
     * @param bomList 集合
     */
    void save(List<Bom> bomList);

    /**
     * 查询机种
     * @param page 页数
     * @param limit 分页大小
     * @param plant 厂别
     * @param product 机种
     * @return Page<Map>
     */
    Page<String> searchProduct(int page, int limit, String plant, String product);

    /**
     * 获取BOM机种下的物料组
     * @param plant 厂别
     * @param product 机种
     * @return
     */
    List<String> getMaterialGroup(String plant, String product);

    /**
     * 获取BOM物料组下的料号
     * @param plant 厂别
     * @param product 机种
     * @param materialGroup 物料组
     * @return
     */
    List<String> getMaterial(String plant, String product, String materialGroup);
}
