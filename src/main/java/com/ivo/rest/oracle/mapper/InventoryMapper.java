package com.ivo.rest.oracle.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Repository
public interface InventoryMapper {

    /**
     * 获取LCM1良品仓材料的库存
     * @param material 料号
     * @param fabDate 日期
     * @return
     */
    Double getLcm1GoodInventory(@Param("material") String material, @Param("fabDate") String fabDate);

    /**
     * 获取LCM1呆滞料的库存
     * @param material 料号
     * @param fabDate 日期
     * @return
     */
    Double getLcm1DullInventory(@Param("material") String material, @Param("fabDate") String fabDate);


    /**
     * 获取LCM2良品仓材料的库存
     * @param material 料号
     * @param fabDate 日期
     * @return
     */
    Double getLcm2GoodInventory(@Param("material") String material, @Param("fabDate") String fabDate);

    /**
     * 获取LCM2呆滞料的库存
     * @param material 料号
     * @param fabDate 日期
     * @return
     */
    Double getLcm2DullInventory(@Param("material") String material, @Param("fabDate") String fabDate);

    /**
     * 获取CELL良品仓材料的库存
     * @param material 料号
     * @param fabDate 日期
     * @return
     */
    Double getCellGoodInventory(@Param("material") String material, @Param("fabDate") String fabDate);


    /**
     * 获取Cell呆滞料的库存
     * @param material 料号
     * @param fabDate 日期
     * @return
     */
    Double getCellDullInventory(@Param("material") String material, @Param("fabDate") String fabDate);



    List<Map> getInventory(@Param("materialList") List<String> materialList, @Param("fabDate") String fabDate,
                           @Param("plant") String plant, @Param("positionList") List<String> positionList);

}
