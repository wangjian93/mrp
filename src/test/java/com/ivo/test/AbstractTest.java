package com.ivo.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

/**
 * 单元测试抽象类
 * @author wj
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(false)
public abstract class AbstractTest {

    /**
     * 开始测试
     */
    @BeforeClass
    public static void setUpForClass() {
        System.out.println(" ");
        System.out.println("++++++++ 开始测试 ++++++++");
    }

    /**
     * 结束测试
     */
    @AfterClass
    public static void testOverForClass() {
        System.out.println(" ");
        System.out.println("-------- 结束测试 --------");
    }
}
