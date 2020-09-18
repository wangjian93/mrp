package com.ivo.rest.dpsAryCell;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 访问2.75 dps数据库的dps_excel_arycel获取ARY/CELL的数据
 * @author wj
 * @version 1.0
 */
@Repository
public interface DpsAryCellMapper {

    /**
     * 获取CELL/ARY DPS PC上传的版本
     * @return List<String>
     */
    List<String> getDpsCellAryVer();

    /**
     * 获取CELL的DPS数据 (Cell Input)
     * @param ver dps版本
     * @return List<Map>
     */
    List<Map> getDpsCell(@Param("ver") String ver);

    /**
     * 获取ARY的DPS数据 (Array Input)
     * @param ver dps版本
     * @return List<Map>
     */
    List<Map> getDpsAry(@Param("ver") String ver);

    /**
     * 获取ARY OC材料的DPS数据  （Array OC Input）
     * @param ver dps版本
     * @return List<Map>
     */
    List<Map> getDpsAryOc(@Param("ver") String ver);

    /**
     * 获取CELL 包材的DPS数据
     * @param ver dps版本
     * @param productList 机种
     * @return List<Map>
     */
    List<Map> getDpsPackage(@Param("ver") String ver, @Param("productList") List<String> productList);
}
