package com.ivo.rest.eif.mapper;

import com.ivo.mrp2.entity.Bom;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 获取Auto PR的BOM
 * @author wj
 * @version 1.0
 */
@Repository
public interface BomMapper {
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
