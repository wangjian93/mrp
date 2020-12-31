package com.ivo.test.service;

import com.ivo.mrp.entity.lcmPackaging.AloneMaterial;
import com.ivo.mrp.repository.AloneMaterialRepository;
import com.ivo.mrp.service.BomPackageLcmService;
import com.ivo.mrp.service.MaterialGroupService;
import com.ivo.mrp.service.MaterialService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public class BomPackageLcmServiceTest extends AbstractTest {

    @Autowired
    private BomPackageLcmService bomPackageLcmService;

    @Autowired
    private AloneMaterialRepository aloneMaterialRepository;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private MaterialGroupService materialGroupService;

    @Test
    public void test() {
        bomPackageLcmService.syncAloneMaterial();
    }

    @Test
    public void test2() {
        List<AloneMaterial> aloneMaterialList = aloneMaterialRepository.findAll();
        for(AloneMaterial aloneMaterial : aloneMaterialList) {
            aloneMaterial.setMaterialName(materialService.getMaterialName(aloneMaterial.getMaterial()));
            aloneMaterial.setMaterialGroup(materialService.getMaterialGroup(aloneMaterial.getMaterial()));
            aloneMaterial.setMaterialGroupName(materialGroupService.getMaterialGroupName(aloneMaterial.getMaterialGroup()));
        }
        aloneMaterialRepository.saveAll(aloneMaterialList);
    }
}
