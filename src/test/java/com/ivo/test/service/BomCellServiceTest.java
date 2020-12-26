package com.ivo.test.service;

import com.ivo.mrp.service.cell.BomCellService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author wj
 * @version 1.0
 */
public class BomCellServiceTest extends AbstractTest {

    @Autowired
    private BomCellService bomCellService;


    @Test
    public void test_syncBomCellMaterial() {
        bomCellService.syncBomCellMaterial();
    }


    @Test
    public void test() {
        bomCellService.syncCellMpsMode();
        bomCellService.syncBomCellProduct();
        bomCellService.syncBomCell2();
    }

}
