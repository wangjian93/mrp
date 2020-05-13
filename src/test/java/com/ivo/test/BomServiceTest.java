package com.ivo.test;

import com.ivo.mrp2.service.BomService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wj
 * @version 1.0
 */
public class BomServiceTest extends AbstractTest {

    @Autowired
    private BomService bomService;

    @Test
    public void syncBom() {
        bomService.syncBom();
    }
}
