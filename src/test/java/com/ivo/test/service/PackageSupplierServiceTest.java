package com.ivo.test.service;

import com.ivo.mrp.service.packageing.PackageSupplierService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author wj
 * @version 1.0
 */
public class PackageSupplierServiceTest extends AbstractTest {

    @Autowired
    private PackageSupplierService packageSupplierService;

    @Test
    public void test() {
        String path = "classpath:data/packageSupplier.xlsx";
        File file = null;
        InputStream inputStream = null;
        try {
            file = ResourceUtils.getFile(path);
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        packageSupplierService.importExcel(inputStream, file.getName());
    }
}
