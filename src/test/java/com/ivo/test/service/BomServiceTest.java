package com.ivo.test.service;

import com.ivo.mrp.service.BomService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * bom数据同步测试
 * @author wj
 * @version 1.0
 */
public class BomServiceTest extends AbstractTest {

    @Autowired
    private BomService bomService;

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
    }

    @Test
    public void test() {
        List list = bomService.getLcmBom("R156NWF7-2L1M25", "LCM1");
        List list2 = bomService.getCellBom("A0709 R0");
        list.size();
    }
}
