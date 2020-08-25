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
}
