package com.ivo.test.service;

import com.ivo.common.utils.ExcelUtil;
import com.ivo.mrp.entity.direct.cell.BomCellMtrl;
import com.ivo.mrp.key.BomCellMtrlKey;
import com.ivo.mrp.service.cell.BomCellService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

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
    public void test_syncCellMpsMode() {
        bomCellService.syncCellMpsMode();
    }

    @Test
    public void test_syncBomCellProduct() {
        bomCellService.syncBomCellProduct();
    }

    @Test
    public void test() {
        bomCellService.test();
    }
}
