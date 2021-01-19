package com.ivo.mrp.repository.ary;

import com.ivo.mrp.entity.direct.ary.AryMpsMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface AryMpsModeRepository extends JpaRepository<AryMpsMode, String> {

    @Query(value = "select distinct product from AryMpsMode ")
    List<String> getProduct();

    @Query(value = "select distinct cellMtrl from AryMpsMode where product=:product")
    List<String> getCellMtrlByProduct(@Param("product")  String product);
}
