package com.ivo.test.service;

import com.ivo.common.utils.ExcelUtil;
import com.ivo.mrp.entity.direct.cell.BomCellMtrl;
import com.ivo.mrp.key.BomCellMtrlKey;
import com.ivo.mrp.repository.BomCellMtrlRepository;
import com.ivo.mrp.service.BomService;
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
 * bom数据同步测试
 * @author wj
 * @version 1.0
 */
public class BomServiceTest extends AbstractTest {

    @Autowired
    private BomService bomService;

    @Autowired
    private BomCellMtrlRepository bomCellMtrlRepository;

    @Test
    public void test_syncBomAry() {
        bomService.syncBomAry();
    }

    @Test
    public void test_syncBomLcm() {
        bomService.syncBomLcm();
    }

    @Test
    public void test_syncBomCell() {
        bomService.syncBomCell();
        bomService.syncBomAry();
    }

    @Test
    public void test() {
        List list = bomService.getLcmBom("R156NWF7-2L1M25", "LCM1");
        List list2 = bomService.getCellBom("A0709 R0");
        list.size();
    }

    @Test
    public void test_() {
        String path = "classpath:data/CELL失效料.xlsx";
        File file = null;
        InputStream inputStream = null;
        try {
            file = ResourceUtils.getFile(path);
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<List<Object>> list = ExcelUtil.readExcelFirstSheet(inputStream, "CELL失效料.xlsx");
        list.size();
        for(int i=1; i<list.size(); i++) {
            List<Object> li = list.get(i);
            String cellMtrl =  (String) li.get(0);
            String fab =  (String) li.get(1);
            String material =  (String) li.get(2);
            String product =  (String) li.get(3);

            BomCellMtrlKey bomCellMtrlKey = new BomCellMtrlKey(fab, product, cellMtrl, material);
            BomCellMtrl bomCellMtrl = bomCellMtrlRepository.findById(bomCellMtrlKey).orElse(null);
            if(bomCellMtrl == null) continue;
            bomCellMtrl.setUseFlag(false);
            bomCellMtrlRepository.save(bomCellMtrl);
        }
    }
}
