package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.MrpDataMaster;
import com.ivo.mrp2.key.MrpDataMasterPrimaryKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpDataMasterRepository extends JpaRepository<MrpDataMaster, MrpDataMasterPrimaryKey>, JpaSpecificationExecutor<MrpDataMaster> {

    MrpDataMaster findByMrpVerAndMaterial(String mrpVer, String material);

    @Query(value = "select DISTINCT material from mrp2_data_master where mrp_ver=:mrpVer and material like :searchMaterial ", nativeQuery = true)
    List<String> getMaterial(@Param("mrpVer") String mrpVer, @Param("searchMaterial") String searchMaterial);

    @Query(value = "select DISTINCT material_group from mrp2_data_master where mrp_ver=:mrpVer and material_group like :searchMaterialGroup ", nativeQuery = true)
    List<String> getMaterialGroup(@Param("mrpVer") String mrpVer, @Param("searchMaterialGroup") String searchMaterialGroup);

    @Query(value = "select DISTINCT d.product from mrp2_dps_data d where d.dps_ver in :dpsVerList and d.product like :searchProduct", nativeQuery = true)
    List<String> getProduct(@Param("dpsVerList") List<String> dpsVerList, @Param("searchProduct") String searchProduct);

    @Query(value = "select DISTINCT s.s_name from mrp2_material_supplier m LEFT JOIN mrp2_supplier s on m.supplier_code = s.id " +
            "where m.material in (select DISTINCT material from mrp2_data d where d.mrp_ver=:mrpVer) " +
            "and (s.s_name like :searchSupplier or s.`name` like :searchSupplier)", nativeQuery = true)
    List<String> getSupplier(@Param("mrpVer") String mrpVer, @Param("searchSupplier") String searchSupplier);

    List<MrpDataMaster> findByMrpVer(@Param("mrpVer") String mrpVer);


    @Query(value = "select  material from mrp2_data_master d where d.mrp_ver in :mrpVers " +
            "and d.material like :material and d.material_group like :materialGroup and d.products like :product GROUP BY d.material",
    countQuery = "select  count(*) from mrp2_data_master d where d.mrp_ver in :mrpVers " +
            "and d.material like :material and d.material_group like :materialGroup and d.products like :product GROUP BY d.material",
    nativeQuery = true)
    Page<String> getPage(@Param("mrpVers") List<String> mrpVers, @Param("product") String product, @Param("materialGroup") String materialGroup, @Param("material") String material, Pageable pageable);
}
