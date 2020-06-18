package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.SubstituteMaterial;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * 替代料维护服务
 * @author wj
 * @version 1.0
 */
public interface SubstituteMaterialService {

    /**
     * 分页查询替代料数据
     * @param page 页数
     * @param limit 分页大小
     * @param plant 厂别
     * @param product  机种
     * @param material 料号
     * @param effectFlag 有效
     * @return Page
     */
    Page<SubstituteMaterial> getPageSubstituteMaterial(int page, int limit, String plant, String product, String material, boolean effectFlag);

    /**
     * 失效替代组
     * @param group 替代组
     */
    void abolish(Integer group);

    /**
     * 保存提到料
     * @param plant 厂别
     * @param product 机种
     * @param substituteMaterials 替代料
     */
    void saveSubstituteMaterial(String plant, String product, List<Map> substituteMaterials);

    /**
     * 获取材料的替代比列，没有维护的返回1
     * @param plant 厂别
     * @param product 机种
     * @param material 料号
     * @return 替代比列
     */
    double getMaterialSubstituteRate(String plant, String product, String material);

    /**
     * 获取SubstituteMaterial对象
     * @param plant 厂别
     * @param product 机种
     * @param material 料号
     * @return SubstituteMaterial
     */
    SubstituteMaterial getSubstituteMaterial(String plant, String product, String material);

    /**
     * 获取替代组的材料集合
     * @param group 替代组
     * @return List<SubstituteMaterial>
     */
    List<SubstituteMaterial> getSubstituteMaterialByGroup(int group);
}
