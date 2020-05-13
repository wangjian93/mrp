package com.ivo.rest.eif.service;

import com.ivo.mrp2.entity.Bom;

import java.util.List;

/**
 * 获取Auto PR的BOM
 * @author wj
 * @version 1.0
 */
public interface EifBomService {

    /**
     * 获取LCM1的BOM
     * @return List<Bom>
     */
    List<Bom> getBomLcm1();

    /**
     * 获取LCM2的BOM
     * @return List<Bom>
     */
    List<Bom> getBomLcm2();

    /**
     * 获取Cell的BOM
     * @return List<Bom>
     */
    List<Bom> getBomCell();
}
