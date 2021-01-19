package com.ivo.test.service;

import com.ivo.mrp.service.cell.CellMaterialService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wj
 * @version 1.0
 */
public class CellMaterialServiceTest extends AbstractTest {

    @Autowired
    private CellMaterialService cellMaterialService;

    @Test
    public void test_syncCellMaterial() {
        cellMaterialService.syncCellMaterial();
    }
}
