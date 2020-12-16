package com.ivo.mrp.repository;

import com.ivo.mrp.entity.ActualArrival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface ActualArrivalRepository extends JpaRepository<ActualArrival, Long>, JpaSpecificationExecutor<ActualArrival> {

    /**
     * 筛选日期
     * @param fabDate 日期
     * @return List<ActualArrival>
     */
    List<ActualArrival> findByFabDate(Date fabDate);

    /**
     * 筛选日期、料号
     * @param fabDate 日期
     * @param material 料号
     * @return
     */
    List<ActualArrival> findByFabDateAndMaterial(Date fabDate, String material);

    /**
     * 获取料号某天的到货数量
     * @param fabDate 日期
     * @param material 料号
     * @return
     */
    @Query(value = "select SUM(a.qty) from ActualArrival a where a.fabDate=:fabDate and a.material=:material")
    Double getActualArrivalQty(@Param("fabDate") Date fabDate, @Param("material") String material);

    /**
     * 筛选日期、料号、工厂
     * @param fabDate 日期
     * @param material 料号
     * @param fab 工厂
     * @return  List<ActualArrival>
     */
    List<ActualArrival> findByFabDateAndMaterialAndWerks(Date fabDate, String material, String fab);

    /**
     * 获取料号某天的到货数量
     * @param fabDate 日期
     * @param material 料号
     * @param fab 工厂
     * @return
     */
    @Query(value = "select SUM(a.qty) from ActualArrival a where a.fabDate=:fabDate and a.material=:material and a.werks=:fab")
    Double getActualArrivalQty(@Param("fabDate") Date fabDate, @Param("material") String material, @Param("fab") String fab);



    List<ActualArrival> findByWerksAndMaterialAndSupplierCodeAndFabDateGreaterThanEqualAndFabDateLessThanEqual(
            String fab, String material, String supplierCode, Date startDate, Date endDate);
}
