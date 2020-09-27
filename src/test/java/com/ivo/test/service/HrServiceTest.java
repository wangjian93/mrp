package com.ivo.test.service;

import com.ivo.rest.HrService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * HRService 测试
 * @author wj
 * @version 1.0
 */
public class HrServiceTest extends AbstractTest {

    @Autowired
    private HrService hrService;

    @Test
    public void test_verify() {
        hrService.verify("c1607908", "ivo@12");
    }

    @Test
    public void test_() {
        hrService.getEmployee("c1607908");
    }
}
