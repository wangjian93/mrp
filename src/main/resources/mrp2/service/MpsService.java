package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.MpsVer;

/**
 * @author wj
 * @version 1.0
 */
public interface MpsService {

    /**
     * 同步MPS
     */
    void syncMps();

    /**
     * 同步LCM的MPS
     */
    void syncMpsLcm();

    /**
     * 同步CELL的MPS
     */
    void syncMpsCell();

    /**
     * 同步Ary的MPS
     */
    void syncMpsAry();



    /**
     * 根据版本获取MPS版本对象
     * @param ver MPS版本
     * @return MpsVer
     */
    MpsVer getMpsVer(String ver);
}
