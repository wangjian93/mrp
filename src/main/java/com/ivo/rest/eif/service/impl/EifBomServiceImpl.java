package com.ivo.rest.eif.service.impl;

import com.ivo.mrp2.entity.Bom;
import com.ivo.rest.eif.mapper.BomMapper;
import com.ivo.rest.eif.service.EifBomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class EifBomServiceImpl implements EifBomService {

    private BomMapper bomMapper;

    @Autowired
    public EifBomServiceImpl(BomMapper bomMapper) {
        this.bomMapper = bomMapper;
    }

    @Override
    public List<Bom> getBomLcm1() {
        log.info("获取EIF数据库LCM1 BOM");
        return bomMapper.getBomLcm1();
    }

    @Override
    public List<Bom> getBomLcm2() {
        log.info("获取EIF数据库LCM2 BOM");
        return bomMapper.getBomLcm2();
    }

    @Override
    public List<Bom> getBomCell() {
        log.info("获取EIF数据库CELL BOM");
        return bomMapper.getBomCell();
    }
}
