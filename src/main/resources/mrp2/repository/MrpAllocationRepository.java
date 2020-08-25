package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.MrpAllocation;
import com.ivo.mrp2.key.MrpAllocationPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpAllocationRepository extends JpaRepository<MrpAllocation, MrpAllocationPrimaryKey> {

    /**
     * 条件查询获取材料某日的分配详细
     * @param plant 厂别
     * @param material 料号
     * @param fabDate 日期
     * @return List<MrpAllocation>
     */
    List<MrpAllocation> findByPlantAndMaterialAndAdate(String plant, String material, Date fabDate);

    /**
     * 获取材料某日分配的数量
     * @param plant 厂别
     * @param material 料号
     * @param fabDate 日期
     * @return Double
     */
    @Query(value = "select sum(a.allocationQty) from MrpAllocation a where a.plant=:plant and a.material=:material and a.fabDate=:fabDate")
    Double getAllocationQty(@Param("plant") String plant, @Param("material") String material, @Param("fabDate") Date fabDate);
}
