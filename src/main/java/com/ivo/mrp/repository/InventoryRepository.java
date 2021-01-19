package com.ivo.mrp.repository;

import com.ivo.mrp.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Page<Inventory> findByFabDateAndMATNRLike(Date fabDate, String material, Pageable pageable);




    @Query(value = "select MATNR as material, SUM(LABST) as qty " +
            "    from mrp3_Inventory " +
            "    WHERE WERKS = :plant " +
            "    AND FAB_DATE= :fabDate " +
            "    AND MATNR IN :materialList" +
            "    AND LGORT IN :positionList" +
            "    GROUP BY MATNR", nativeQuery = true)
    List<Map> getInventoryBatch(@Param("materialList") List<String> materialList,
                          @Param("fabDate") Date fabDate,
                          @Param("plant") String plant,
                          @Param("positionList") List<String> positionList);



    List<Inventory> findByFabDate(Date fabDate);
}
