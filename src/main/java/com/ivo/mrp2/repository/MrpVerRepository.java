package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.MrpVer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpVerRepository extends JpaRepository<MrpVer, String> {

    int countByDpsVer(String dpsVer);

    @Query(value = "select t.mrp_ver from mrp_ver t group by t.mrp_ver ORDER BY t.mrp_ver DESC", nativeQuery = true)
    List<String> getMrpVer();
}
