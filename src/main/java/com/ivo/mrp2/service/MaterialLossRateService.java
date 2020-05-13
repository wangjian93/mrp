package com.ivo.mrp2.service;

/**
 * 材料损耗率
 * @author wj
 * @version 1.0
 */
public interface MaterialLossRateService {

    /**
     * 获取物料的损耗率，当料号没有维护损耗率时将返回物料组的损耗率
     * @param material 料号
     * @return 损耗率
     */
    Double getMaterialLossRate(String material);

}
