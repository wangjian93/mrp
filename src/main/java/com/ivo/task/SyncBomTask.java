package com.ivo.task;

import com.ivo.mrp.service.BomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * BOM数据同步任务
 * @author wj
 * @version 1.0
 */
@Component
@Slf4j
public class SyncBomTask {

    private static final String BOM_LCM_Corn = "0 0 23 * * ?";
    private static final String BOM_ARY_Corn = "0 10 23 * * ?";
    private static final String BOM_CELL_Corn = "0 20 23 * * ?";

    private BomService bomService;

    @Autowired
    public SyncBomTask(BomService bomService) {
        this.bomService = bomService;
    }

    /**
     * 同步LCM BOM
     */
    @Scheduled(cron = BOM_LCM_Corn)
    public void syncBomLcm() {
        log.info("启动任务syncBomLcm");
        bomService.syncBomLcm();
    }

    /**
     * 同步ARY BOM
     */
    @Scheduled(cron = BOM_ARY_Corn)
    public void syncBomAry() {
        log.info("启动任务syncBomAry");
        bomService.syncBomAry();
    }

    /**
     * 同步CELL BOM
     */
    @Scheduled(cron = BOM_CELL_Corn)
    public void syncBomCell() {
        log.info("启动任务syncBomCell");
        bomService.syncBomCell();
    }
}
