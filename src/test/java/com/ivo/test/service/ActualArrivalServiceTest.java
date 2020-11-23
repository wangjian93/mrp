package com.ivo.test.service;

import com.ivo.mrp.service.ActualArrivalService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wj
 * @version 1.0
 */
public class ActualArrivalServiceTest extends AbstractTest {

    @Autowired
    private ActualArrivalService actualArrivalService;

    @Test
    public void test_syncActualArrival() {
        actualArrivalService.syncActualArrival();
    }
}
