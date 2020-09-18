package com.ivo.rest.fcst;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Repository
public interface FcstMapper {

    /**
     * 获取MPS中的CELL BOM成品料号
     * @return List<Map>
     */
    List<Map> getBomCell();
}
