package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.Bom;
import com.ivo.mrp2.key.BomPrimaryKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface BomRepository extends JpaRepository<Bom, BomPrimaryKey> {

    List<Bom> findByPlantAndProduct(String plant, String product);

    @Query(value = "select t.material_group from mrp_bom t where t.material =:material limit 1", nativeQuery = true)
    String getMaterialGroup(String material);

    @Query(value = "select t.material_name from mrp_bom t where t.material =:material and material_name<>'' limit 1", nativeQuery = true)
    String getMaterialName(String material);

    Page<Bom> findByProductLikeAndMaterialLikeAndPlantLike(String product, String material, String plant, Pageable pageable);

    @Query(value = "select material, material_name as materialName, material_group as materialGroup from mrp_bom where material in :materialList", nativeQuery = true)
    List<Map> getMaterialNameAndGroup(List<String> materialList);
}
