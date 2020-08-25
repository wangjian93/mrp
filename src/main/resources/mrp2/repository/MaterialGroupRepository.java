package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.MaterialGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MaterialGroupRepository extends JpaRepository<MaterialGroup, String> {

    /**
     * 查询所有物料组
     * @return List<String>
     */
    @Query(value = "select material_group from mrp2_material_group m where m.material_group like :search limit :limit", nativeQuery = true)
    List<String> searchMaterialGroup(@Param("search") String search, @Param("limit") int limit);
}
