package com.ivo.rest.eif;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 访问81数据库EIF DB
 * @author wj
 * @version 1.0
 */
@Repository
public interface EifMapper {

    /**
     * 获取料号数据
     * @return List<Map>
     */
    List<Map> getMaterial();

    /**
     * 获取物料组数据
     * @return List<Map>
     */
    List<Map> getMaterialGroup();

    /**
     * 获取机种数据
     * @return List<Map>
     */
    List<Map> getProject();

    /**
     * 获取LCM1的BOM
     * @return  List<Map>
     */
    List<Map> getBomLcm1();

    /**
     * 获取LCM2的BOM
     * @return List<Map>
     */
    List<Map> getBomLcm2();

    /**
     * 获取ARY的BOM
     * @return List<Map>
     */
    List<Map> getBomAry();

    /**
     * 获取LCM1的替代料关系
     * @return List<Map>
     */
    List<Map> getMaterialSubstituteLcm1();

    /**
     * 获取LCM2的替代料关系
     * @return List<Map>
     */
    List<Map> getMaterialSubstituteLcm2();

    /**
     * 获取CELL的替代料关系
     * @return List<Map>
     */
    List<Map> getMaterialSubstituteCell();
}
