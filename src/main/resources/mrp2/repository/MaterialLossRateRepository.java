package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.MaterialLossRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * @author wj
 * @version 1.0
 */
public interface MaterialLossRateRepository extends JpaRepository<MaterialLossRate, Long> {

    /**
     * 获取料号的有效损耗率
     * @param material 料号
     * @param effectFlag 有效性
     * @return MaterialLossRate
     */
    MaterialLossRate findFirstByMaterialAndEffectFlag(String material, boolean effectFlag);

    /**
     * 获取物料组的有效损耗率
     * @param materialGroup 物料组
     * @param effectFlag 有效性
     * @return MaterialLossRate
     */
    MaterialLossRate findFirstByMaterialGroupAndEffectFlag(String materialGroup, boolean effectFlag);

    List<MaterialLossRate> findByEffectFlagIsTrue();






    /**
     * 查询料号的材料损耗率
     * @param material 料号
     * @return MaterialLossRate
     */
    MaterialLossRate findFirstByMaterialAndType(String material, int type);

    /**
     * 查询物料组的材料损耗率
     * @param materialGroup 物料组
     * @return MaterialLossRate
     */
    MaterialLossRate findFirstByMaterialGroupAndType(String materialGroup, int type);

    /**
     * 分页查询材料损耗率
     * @param materialGroup 物料组
     * @param material 料号
     * @param pageable 分页
     * @return Page<MaterialLossRate>
     */
    Page<MaterialLossRate> findByMaterialGroupLikeAndMaterialLike(String materialGroup, String material, Pageable pageable);
}
