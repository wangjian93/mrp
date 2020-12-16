package com.ivo.test.service;

import com.ivo.mrp.service.packageing.BomPackageService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * cell包材BOM测试
 * @author wj
 * @version 1.0
 */
public class BomPackageServiceTest extends AbstractTest {

    @Autowired
    private BomPackageService bomPackageService;

    @Test
    public void test() {
        String path = "classpath:data/BOM-list.xlsx";
        File file = null;
        InputStream inputStream = null;
        try {
            file = ResourceUtils.getFile(path);
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bomPackageService.importBomPackage(inputStream, file.getName());
    }
}
