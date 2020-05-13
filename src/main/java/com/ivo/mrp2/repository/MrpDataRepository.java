package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.MrpData;
import com.ivo.mrp2.key.MrpDataPrimaryKey;
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

    MrpData findByMrpVerAndFabDateAndMaterial(String mrpVer, Date fabDate, String material);

    @Query(value = "select DISTINCT material from mrp_data t where t.mrp_ver=:mrpVer", nativeQuery = true)
    List<String> getMaterial(String mrpVer);
}
