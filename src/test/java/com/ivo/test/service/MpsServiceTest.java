package com.ivo.test.service;

import com.ivo.mrp.service.MpsService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * MSP数据上传同步测试
 * @author wj
 * @version 1.0
 */
public class MpsServiceTest extends AbstractTest {

    @Autowired
    private MpsService mpsService;

    @Test
    public void test_importMpsLcm() {
        String path = "classpath:data/LCM_MPS.xlsx";
        File file = null;
        InputStream inputStream = null;
        try {
            file = ResourceUtils.getFile(path);
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mpsService.importMpsLcm(inputStream, file.getName(), "SYS");
    }
}
