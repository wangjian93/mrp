package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.MaterialSubstitute;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * 替代料维护服务
 * @author wj
 * @version 1.0
 */
public interface MaterialSubstituteService {

    /**
     * 从BOM AutoPR同步替代料
     */
    void syncMaterialSubstitute();

    /**
     * 保存替代料
     * @param materialSubstituteList 集合
     */
    void save(List<MaterialSubstitute> materialSubstituteList);

    /**
     * 获取材料替代比例，如果没有替代返回1
     * @param plant 厂别
     * @param product 机种
     * @param material 料号
     * @return 替代比列
     */
    double getMaterialSubstituteRate(String plant, String product, String material);

    /**
     * 分页查询替代料
     * @param page 页数
     * @param limit 分页大小
     * @param plant 厂别
     * @param product 机种
     * @param materialGroup 物料组
     * @return Page<MaterialSubstitute>
     */
    Page<MaterialSubstitute> getMaterialSubstitute(int page, int limit, String plant, String product, String materialGroup);

    /**
     * 获取一组替代料
     * @param plant 厂别
     * @param product 机种
     * @param materialGroup 物料组
     * @return List<MaterialSubstitute>
     */
    List<MaterialSubstitute> getMaterialSubstitute(String plant, String product, String materialGroup);

    /**
     * 保存一组替代料
     * @param plant 厂别
     * @param product 机种
     * @param materialGroup 物料组
     * @param materialSubstituteMap 材料的替代比例
     */
    void saveMaterialSubstitute(String plant, String product, String materialGroup, Map<String, Double> materialSubstituteMap);
}
