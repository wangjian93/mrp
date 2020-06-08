package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.MrpData;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpDataService {

    /**
     * 获取MRP版本的缺料数据
     * @param mrpVer MRP版本
     * @return List<MrpData>
     */
    List<MrpData> getShortMrpData(String mrpVer);
}
