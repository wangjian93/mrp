package com.ivo.rest.dps.service;

import com.ivo.mrp2.entity.Dps;
import com.ivo.rest.dps.entity.RestDps;

import java.util.List;

/**
 * 提供DPS获取数据服务接口
 * @author wj
 * @version 1.0
 */
public interface RestDpsService {

    /**
     * 按版本获取DPS
     * @param ver 版本
     * @return
     */
    List<RestDps> getDpsByVer(String ver);

    /**
     * 按版本获取DPS
     * @param ver 版本
     * @return
     */
    List<Dps> getDpsByVer2(String ver);

    /**
     * 获取DPS所有的版本
     * @return List<String>
     */
    List<String> getDpsVer();
}
