package com.ivo.mrp.repository.lcmPackage;

import com.ivo.mrp.entity.lcmPackaging.DemandPackageLcm;
import com.ivo.mrp.key.DemandKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface DemandPackageLcmRepository extends JpaRepository<DemandPackageLcm, DemandKey> {

    @Query(value = "select distinct d.material from DemandPackageLcm d where d.ver=:ver and d.isAlone = true")
    List<String> getDemandAloneMaterial(String ver);

    @Query(value = "select distinct d.product as product, d.material as material from DemandPackageLcm d where d.ver=:ver and d.isAlone = false")
    List<Map> getDemandProductMaterial(String ver);



    @Query(value = "select d.fabDate as fabDate, sum(d.demandQty) as demandQty from DemandPackageLcm d where d.ver=:ver and d.material=:material and d.demandQty>0 and d.isAlone=true group by d.fabDate")
    List<Map> getDemandQtyAlone(@Param("ver") String ver, @Param("material") String material);


    @Query(value = "select d.fabDate as fabDate, sum(d.demandQty) as demandQty from DemandPackageLcm d where d.ver=:ver and d.product=:product and d.material=:material and d.demandQty>0 and d.isAlone=false group by d.fabDate")
    List<Map> getDemandQtyProduct(@Param("ver") String ver, @Param("product") String product, @Param("material") String material);


    @Query(value = "select distinct d.product from DemandPackageLcm d where d.ver=:ver and d.material=:material and d.isAlone=true")
    List<String> getDemandAloneMaterialProduct(@Param("ver") String ver, @Param("material") String material);

    @Query(value = "select d.fabDate as fabDate, d.product as product, sum(d.demandQty) as demandQty from DemandPackageLcm d where d.ver=:ver and d.material=:material and d.demandQty>0 and d.isAlone=true group by d.fabDate,d.product")
    List<Map> getDemandQtyAloneDetail(@Param("ver") String ver, @Param("material") String material);
}
