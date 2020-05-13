package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.Dps;
import com.ivo.mrp2.key.DpsPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.validation.annotation.Validated;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface DpsRepository extends JpaRepository<Dps, DpsPrimaryKey> {

    List<Dps> findByDpsVer(String dpsVer);

    List<Dps> findByDpsVerAndProduct(String dpsVer, String product);

    Dps findTopByDpsVer(String dpsVer);

    @Query(value = "select min(fab_date) as startDate, max(fab_date) as endDate from mrp_dps t where t.dps_ver=:dpsVer", nativeQuery = true)
    Map<String, Date> getDpsDateRange(String dpsVer);

    @Query(value = "select dps_ver from mrp_dps group by dps_ver ORDER BY dps_ver DESC", nativeQuery = true)
    List<String> getDpsVer();

    @Query(value = "SELECT DISTINCT product from mrp_dps t where t.dps_ver=:dpsVer", nativeQuery = true)
    List<String> getProduct(String dpsVer);
}
