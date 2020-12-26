package com.ivo.test.service;

import com.ivo.mrp.entity.pol.BomPol;
import com.ivo.mrp.repository.pol.BomPolRepository;
import com.ivo.mrp.service.MaterialService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public class BomPolServiceTest extends AbstractTest {

    @Autowired
    private BomPolRepository bomPolRepository;
    @Autowired
    private MaterialService materialService;

    @Test
    public void test() {
        List<BomPol> bomPolList = bomPolRepository.findAll();
        for(BomPol bomPol : bomPolList) {
            bomPol.setMaterialName(materialService.getMaterialName(bomPol.getMaterial()));
        }
        bomPolRepository.saveAll(bomPolList);
    }
}
