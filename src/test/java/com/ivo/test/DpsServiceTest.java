package com.ivo.test;

import com.ivo.mrp2.service.DpsService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author wj
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DpsServiceTest extends AbstractTest {

    @Autowired
    private DpsService dpsService;

    @Test
    public void syncDps() {
        dpsService.syncDps();
    }

    @Test
    public void importDps() {
        String path = "/Users/wangjian/Downloads/LCM1.xlsx";
        File file = new File(path);
        try {
            InputStream inputStream = new FileInputStream(file);
            dpsService.importDps(inputStream,path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
