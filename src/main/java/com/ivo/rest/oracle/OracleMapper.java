package com.ivo.rest.oracle;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Repository
public interface OracleMapper {

    /**
     * 获取成品料号的材料
     * @param cellMtrl 成品料号
     * @return List<Map>
     */
    List<Map> getCellMtrl(@Param("cellMtrl") String cellMtrl);

    /**
     * 获取CELL料号的材料
     * @return List<Map>
     */
    List<Map> getCellMaterial();



    /**
     * 获取材料库存
     * @param material 料号
     * @param fabDate 日期
     * @param plant 工厂
     * @param positionList 仓库
     * @return Double
     */
    Double getInventory(@Param("material") String material,
                        @Param("fabDate") String fabDate,
                        @Param("plant") String plant,
                        @Param("positionList") List<String> positionList);

    /**
     * 批量获取材料库存
     * @param materialList 料号
     * @param fabDate 日期
     * @param plant 工厂
     * @param positionList 仓库
     * @return List<Map>
     */
    List<Map> getInventoryBatch(@Param("materialList") List<String> materialList,
                                @Param("fabDate") String fabDate,
                                @Param("plant") String plant,
                                @Param("positionList") List<String> positionList);


    List<Map> getInventoryAll(@Param("fabDate") String fabDate, @Param("positionList") List<String> positionList);

}
