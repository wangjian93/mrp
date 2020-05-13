package com.ivo.rest.dps.mapper;

import com.ivo.rest.dps.entity.RestDps;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Repository
public interface DpsMapper {
    /**
     * 按版本获取DPS
     * @param ver 版本
     * @return
     */
    List<RestDps> getDpsByVer(String ver);

    /**
     * 获取DPS所有的版本
     * @return List<String>
     */
    List<String> getDpsVer();
}
