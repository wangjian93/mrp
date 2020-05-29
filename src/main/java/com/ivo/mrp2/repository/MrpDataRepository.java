package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.MrpData;
import com.ivo.mrp2.key.MrpDataPrimaryKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpDataRepository extends JpaRepository<MrpData, MrpDataPrimaryKey> {

    List<MrpData> findByMrpVer(String mrpVer);

    List<MrpData> findByMrpVerAndMaterial(String mrpVer, String material);

    List<MrpData> findByMrpVerAndMaterialIn(String mrpVer, List<String> materials);

    MrpData findByMrpVerAndFabDateAndMaterial(String mrpVer, Date fabDate, String material);

    @Query(value = "select DISTINCT material from mrp_data t where t.mrp_ver=:mrpVer", nativeQuery = true)
    List<String> getMaterial(String mrpVer);

    @Query(value = "select DISTINCT material from mrp_data t where t.mrp_ver=:mrpVer",
            countQuery = "select count(DISTINCT material) from mrp_data t where t.mrp_ver=:mrpVer",
            nativeQuery = true)
    Page<String> getMaterial(String mrpVer, Pageable pageable);

    @Query(value = "select DISTINCT material from mrp_demand t where t.dps_ver = (select dps_ver from mrp2_mrp_ver where mrp_ver=:mrpVer) and product =:product",
            countQuery = "select count(DISTINCT material) from mrp_demand t where t.dps_ver = (select dps_ver from mrp2_mrp_ver where mrp_ver=:mrpVer) and product =:product",
            nativeQuery = true)
    Page<String> getMaterialByProduct(String mrpVer, String product, Pageable pageable);


//
//
//    @Query(value = "select DISTINCT material from mrp_data t where t.mrp_ver=:mrpVer and t.products like :product and t.material=:material",
//            countQuery = "select count(DISTINCT material) from mrp_data t where t.mrp_ver=:mrpVer and t.products like :product and t.material=:material",
//            nativeQuery = true)
//    Page<String> getMaterial(String mrpVer, String product, String material, Pageable pageable);
//
//    @Query(value = "select DISTINCT material from mrp_data t where t.mrp_ver=:mrpVer and t.material=:material",
//            countQuery = "select count(DISTINCT material) from mrp_data t where t.mrp_ver=:mrpVer and t.material=:material",
//            nativeQuery = true)
//    Page<String> getMaterialByMaterial(String mrpVer, String material, Pageable pageable);
}
