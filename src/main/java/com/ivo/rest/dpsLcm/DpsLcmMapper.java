package com.ivo.rest.dpsLcm;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 访问2.115 dps_lcm的dps_bpc获取LCM的DPS
 * @author wj
 * @version 1.0
 */
@Repository
public interface DpsLcmMapper {

    /**
     * 获取LCM DPS数据
     * @param ver 版本
     * @return List<Map>
     */
    List<Map> getDpsLcm(@Param("ver") String ver);

    /**
     * 获取LCM DPS的版本
     * @return List<String>
     */
    List<String> getDpsLcmVer();
}
