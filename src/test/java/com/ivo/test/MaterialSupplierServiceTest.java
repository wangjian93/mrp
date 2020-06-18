package com.ivo.test;

import com.ivo.mrp2.entity.MaterialSupplier;
import com.ivo.mrp2.repository.MaterialSupplierRepository;
import com.ivo.mrp2.service.MaterialSupplierService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */

public class MaterialSupplierServiceTest extends AbstractTest {

    @Autowired
    private MaterialSupplierService materialSupplierService;

    @Autowired
    private MaterialSupplierRepository materialSupplierRepository;

    @Test
    public void test() {
        List<MaterialSupplier> materialSupplierList =  materialSupplierRepository.findAll();
        for(MaterialSupplier materialSupplier : materialSupplierList) {
            materialSupplierService.syncMaterialGroupAndMaterialName(materialSupplier);
        }
        materialSupplierRepository.saveAll(materialSupplierList);
    }
}
