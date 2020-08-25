package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.MaterialSubstitute;
import com.ivo.mrp2.key.MaterialSubstituteKey;
import org.hibernate.annotations.SQLInsert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MaterialSubstituteRepository extends JpaRepository<MaterialSubstitute, MaterialSubstituteKey>, JpaSpecificationExecutor<MaterialSubstitute> {

    /**
     * 清空表
     */
    @Transactional
    @Modifying
    @Query(value = "truncate table mrp2_material_substitute", nativeQuery = true)
    void truncate();

    /**
     * 获取一组替代料
     * @param plant 厂别
     * @param product 机种
     * @param materialGroup 物料组
     * @return List<MaterialSubstitute>
     */
    List<MaterialSubstitute> findByPlantAndProductAndMaterialGroup(String plant, String product, String materialGroup);
}