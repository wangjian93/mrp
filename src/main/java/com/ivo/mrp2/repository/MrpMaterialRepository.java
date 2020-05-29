package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.MrpMaterial;
import com.ivo.mrp2.key.MrpMaterialPrimaryKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpMaterialRepository extends JpaRepository<MrpMaterial, MrpMaterialPrimaryKey> {

    MrpMaterial findByMrpVerAndMaterial(String mrpVer, String material);

    @Query(value = "select material from mrp_material where mrp_ver=:mrpVer ", nativeQuery = true)
    List<String> getMaterial(String mrpVer);


    @Query(value = "select distinct material_group from mrp_material where mrp_ver=:mrpVer ", nativeQuery = true)
    List<String> getMaterialGroup(String mrpVer);

    @Query(value = "from mrp_material t where t.mrpVer=:mrpVer and t.material like :material and t.products like :product and t.materialGroup like :materialGroup",
    countQuery = "select count(distinct material) from mrp_material t where t.mrpVer=:mrpVer and t.material like :material and t.products like :product and t.materialGroup like :materialGroup")
    Page<MrpMaterial> getPage(String mrpVer, String product, String materialGroup, String material, Pageable pageable);
}
