package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.DpsData;
import com.ivo.mrp2.key.DpsPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface DpsDataRepository extends JpaRepository<DpsData, DpsPrimaryKey> {

    /**
     * 获取某一版本的DPS数据
     * @param ver dps版本
     * @return  List<MrpData>
     */
    List<DpsData> findByDpsVer(String ver);

    /**
     * 获取DPS下所有机种
     * @param dpsVer dps版本
     * @return List<String>
     */
    @Query(value = "SELECT DISTINCT product from mrp2_dps_data t where t.dps_ver=:dpsVer", nativeQuery = true)
    List<String> getProduct(@Param("dpsVer") String dpsVer);

    /**
     * 获取DPS下机种的数据
     * @param dpsVer DPS版本
     * @param product 机种
     * @return
     */
    List<DpsData> findByDpsVerAndProduct(String dpsVer, String product);
}
