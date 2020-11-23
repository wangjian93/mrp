package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.Allocation;
import com.ivo.mrp.key.AllocationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface AllocationRepository extends JpaRepository<Allocation, AllocationKey> {

    /**
     * 获取总的分配量
     * @param fab 厂别
     * @param material 料号
     * @param fabDate 日期
     * @return Double
     */
    @Query(value = "select sum(a.allocationQty) from Allocation a where a.fab=:fab and a.material=:material and a.fabDate=:fabDate ")
    Double getAllocation(@Param("fab") String fab, @Param("material") String material, @Param("fabDate") Date fabDate);

    /**
     * 获取总的分配量
     * @param fab 厂别
     * @param material 料号
     * @param fabDateList 日期集合
     * @return Double
     */
    @Query(value = "select a.fabDate as fabDate, sum(a.allocationQty) as allocationQty from Allocation a where a.fab=:fab and a.material=:material and a.fabDate in :fabDateList group by a.fabDate")
    List<Map> getAllocation(@Param("fab") String fab, @Param("material") String material,  @Param("fabDateList") List<Date> fabDateList);

    /**
     * 筛选厂别、料号、日期
     * @param fab 厂别
     * @param material 料号
     * @param fabDate 日期
     * @return List<Allocation>
     */
    List<Allocation> findByFabAndMaterialAndFabDate(String fab, String material, Date fabDate);


    List<Allocation> findByFabAndMaterialAndSupplierCodeAndFabDateGreaterThanEqualAndFabDateLessThanEqual(
            String fab, String material, String supplierCode, Date startDate, Date endDate
    );
}
