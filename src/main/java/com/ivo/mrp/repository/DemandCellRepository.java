package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.ary.DemandAry;
import com.ivo.mrp.entity.direct.cell.DemandCell;
import com.ivo.mrp.key.DemandKey;
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
public interface DemandCellRepository extends JpaRepository<DemandCell, DemandKey> {

    /**
     * 获取MRP需求所有的料号
     * @param ver MRP版本
     * @return List<String>
     */
    @Query(value = "select DISTINCT d.material from DemandCell d where d.ver=:ver")
    List<String> getMaterial(@Param("ver") String ver);

    /**
     * 汇总料号需求
     * @param ver MRP版本
     * @param material 料号
     * @return List<Map<Date, Double>>
     */
    @Query(value = "select d.fabDate as fabDate, sum(d.demandQty) as demandQty from DemandCell d where d.ver=:ver and d.material=:material and d.demandQty>0 group by d.fabDate")
    List<Map> getDemandQtyCell(@Param("ver") String ver, @Param("material") String material);

    /**
     * 汇总料号、日期需求
     * @param ver MRP版本
     * @param material 料号
     * @param fabDate 日期
     * @return Double
     */
    @Query(value = "select sum(d.demandQty) as demandQty from DemandCell d where d.ver=:ver and d.material=:material and d.demandQty>0 and d.fabDate=:fabDate")
    Double getDemandQtyCell(@Param("ver") String ver, @Param("material") String material,@Param("fabDate")  Date fabDate);

    /**
     * 筛选MRP版本、料号、日期
     * @param ver 版本
     * @param material 料号
     * @param fabDate 日期
     * @return List<DemandLcm>
     */
    List<DemandCell> findByVerAndMaterialAndFabDate(String ver, String material, Date fabDate);

    /**
     * 获取料号的需求机种
     * @param ver MRP版本
     * @return List<String>
     */
    @Query(value = "select DISTINCT d.product from DemandCell d where d.ver=:ver and d.material=:material")
    List<String> getProduct(@Param("ver") String ver, @Param("material") String material);
}
