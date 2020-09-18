package com.ivo.test.service;

import com.ivo.mrp.service.SubstituteService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 替代料数据同步测试
 * @author wj
 * @version 1.0
 */
public class SubstituteServiceTest extends AbstractTest {

    @Autowired
    private SubstituteService substituteService;


    @Test
    public void test_syncSubstitute() {
        substituteService.syncSubstitute();
    }
}
