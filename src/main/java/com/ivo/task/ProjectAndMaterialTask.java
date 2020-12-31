package com.ivo.task;

import com.ivo.mrp.service.MaterialGroupService;
import com.ivo.mrp.service.MaterialService;
import com.ivo.mrp.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 机种和物料、物料组数据同步任务
 * @author wj
 * @version 1.0
 */
@Component
@Slf4j
public class ProjectAndMaterialTask {

    private static final String Project_Cron = "0 20 22 * * ?";
    private static final String MaterialGroup_Cron = "0 30 22 * * ?";
    private static final String Material_Cron = "0 40 22 * * ?";


    private ProjectService projectService;

    private MaterialService materialService;

    private MaterialGroupService materialGroupService;

    @Autowired
    public ProjectAndMaterialTask(ProjectService projectService,
                                  MaterialService materialService,
                                  MaterialGroupService materialGroupService) {
        this.projectService = projectService;
        this.materialService = materialService;
        this.materialGroupService = materialGroupService;
    }

    /**
     * 同步机种
     */
//    @Scheduled(cron = Project_Cron)
    public void syncProject(){
        log.info("启动任务syncProject");
        projectService.syncProject();
    }

    /**
     * 同步物料组
     */
//    @Scheduled(cron = MaterialGroup_Cron)
    public void syncMaterialGroup(){
        log.info("启动任务syncMaterialGroup");
        materialGroupService.syncMaterialGroup();
    }

    /**
     * 同步料号
     */
//    @Scheduled(cron = Material_Cron)
    public void syncMaterial(){
        log.info("启动任务syncMaterial");
        materialService.syncMaterial();
    }
}
