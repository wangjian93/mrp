package com.ivo.test.service;

import com.ivo.mrp.service.SupplierService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 供应商测试
 * @author wj
 * @version 1.0
 */
public class SupplierServiceTest extends AbstractTest {

    @Autowired
    private SupplierService supplierService;

    @Test
    public void test() {
        String path = "classpath:data/材料与供应商数据.xlsx";
        File file = null;
        InputStream inputStream = null;
        try {
            file = ResourceUtils.getFile(path);
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        supplierService.importSupplierMaterial(inputStream, file.getName());
    }
}
