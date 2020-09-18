package com.ivo.mrp.repository;

import com.ivo.mrp.entity.LossRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author wj
 * @version 1.0
 */
public interface LossRateRepository extends JpaRepository<LossRate, Long>, JpaSpecificationExecutor<LossRate> {

    /**
     * 筛选料号
     * @param material 料号
     * @param type 类型
     * @param validFlag 有效性标识
     * @return LossRate
     */
    LossRate findFirstByMaterialAndTypeAndValidFlag(String material, int type, boolean validFlag);

    /**
     * 筛选物料组
     * @param materialGroup 物料组
     * @param type 类型
     * @param validFlag 有效性标识
     * @return LossRate
     */
    LossRate findFirstByMaterialGroupAndTypeAndValidFlag(String materialGroup, int type, boolean validFlag);
}
