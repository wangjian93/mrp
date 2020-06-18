package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.MaterialLossRate;
import org.springframework.data.jpa.repository.JpaRepository;


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
}
