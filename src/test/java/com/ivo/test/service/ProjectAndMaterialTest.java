package com.ivo.test.service;

import com.ivo.mrp.service.MaterialGroupService;
import com.ivo.mrp.service.MaterialService;
import com.ivo.mrp.service.ProjectService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 机种与料号数据同步测试
 * @author wj
 * @version 1.0
 */
public class ProjectAndMaterialTest extends AbstractTest {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private MaterialGroupService materialGroupService;

    @Autowired
    private ProjectService projectService;

    @Test
    public void test_syncMaterial() {
        materialService.syncMaterial();
    }

    @Test
    public void test_syncMaterialGroup() {
        materialGroupService.syncMaterialGroup();
    }

    @Test
    public void test_syncProject() {
        projectService.syncProject();
    }
}
