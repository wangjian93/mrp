package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.MaterialLossRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;

/**
 * @author wj
 * @version 1.0
 */
public interface MaterialLossRateRepository extends JpaRepository<MaterialLossRate, Long> {

    /**
     * 获取料号的有效损耗率
     * @param material 料号
     * @param date 日期
     * @return MaterialLossRate
     */
    @Query("from mrp_material_loss_rate a where a.material=:material and a.effectDate<=:date and a.expireDate>:date")
    MaterialLossRate getEffectRate(String material, Date date);

    /**
     * 获取物料组的有效损耗率
     * @param materialGroup 物料组
     * @param date 日期
     * @return MaterialLossRate
     */
    @Query("from mrp_material_loss_rate a where a.materialGroup=:materialGroup and a.material=null and a.effectDate<=:date and a.expireDate>:date")
    MaterialLossRate getEffectRateByMaterialGroup(String materialGroup, Date date);

}
