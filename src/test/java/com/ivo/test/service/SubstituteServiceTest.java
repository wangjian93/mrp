package com.ivo.test.service;

import com.ivo.common.utils.ExcelUtil;
import com.ivo.mrp.entity.Substitute;
import com.ivo.mrp.repository.SubstituteRepository;
import com.ivo.mrp.service.SubstituteService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

/**
 * 替代料数据同步测试
 * @author wj
 * @version 1.0
 */
public class SubstituteServiceTest extends AbstractTest {

    @Autowired
    private SubstituteService substituteService;

    @Autowired
    private SubstituteRepository substituteRepository;


    @Test
    public void test_syncSubstitute() {
        substituteService.syncSubstitute();
    }

    @Test
    public void test() {

        String path = "/Users/wangjian/Downloads/cell替代料.xlsx";
        File file = null;
        InputStream inputStream = null;
        try {
            file = ResourceUtils.getFile(path);
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        List<List<Object>> list = ExcelUtil.readExcelFirstSheet(inputStream, file.getName());
        list.size();

        for(int i=1; i<list.size(); i++) {
            System.out.println(i);
            List<Object> row = list.get(i);
            String product = (String)row.get(0);
            String materialGroup = (row.get(1)).toString();
            String material = row.get(2).toString();
            double rate = ((BigDecimal)row.get(3)).doubleValue();
            Substitute substitute = substituteService.getSubstitute("CELL", product, materialGroup, material);
            if(substitute == null) continue;
            substitute.setSubstituteRate(rate);
            substituteRepository.save(substitute);
        }
    }
}
