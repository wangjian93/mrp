package com.ivo.mrp.repository.lcmPackage;

import com.ivo.mrp.entity.lcmPackaging.MrpPackageLcm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpPackageLcmRepository extends JpaRepository<MrpPackageLcm, Long> {

    List<MrpPackageLcm> findByVerAndMaterialOrderByFabDateAsc(String ver, String material);

    List<MrpPackageLcm> findByVerAndProductAndMaterialOrderByFabDateAsc(String ver, String product, String material);


    MrpPackageLcm findByVerAndMaterialAndFabDate(String ver, String material, Date fabDate);

    MrpPackageLcm findByVerAndProductAndMaterialAndFabDate(String ver, String product, String material, Date fabDate);

}
