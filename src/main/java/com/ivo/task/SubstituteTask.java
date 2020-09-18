package com.ivo.task;

import com.ivo.mrp.service.SubstituteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 替代料数据同步任务
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class SubstituteTask {

    private static final String Substitute_Corn = "0 0 21 * * ?";

    private SubstituteService substituteService;

    @Autowired
    public SubstituteTask(SubstituteService substituteService) {
        this.substituteService = substituteService;
    }

    @Scheduled(cron = Substitute_Corn)
    public void syncSubstitute() {
        log.info("启动任务syncSubstitute");
        substituteService.syncSubstitute();
    }
}
