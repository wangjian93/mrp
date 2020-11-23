package com.ivo.mrp.service;

import com.ivo.mrp.entity.MrpWarn;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpWarnService {

    /**
     * 添加提醒
     * @param ver
     * @param product
     * @param type
     * @param memo
     */
    void addWarn(String ver, String product, String type, String memo);

    /**
     * 获取提醒
     * @param ver
     * @return List<MrpWarn>
     */
    List<MrpWarn> getMrpWarn(String ver);

}
