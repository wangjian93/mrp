package com.ivo.test.service;

import com.ivo.mrp.service.CutService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * 切片数获取测试
 * @author wj
 * @version 1.0
 */
public class CutServiceTest extends AbstractTest {

    @Autowired
    private CutService cutService;

    @Test
    public void getProjectCut() {
        Double cut = cutService.getProjectCut("A0282");
        Assert.notNull(cut, "测试失败");
    }
}
