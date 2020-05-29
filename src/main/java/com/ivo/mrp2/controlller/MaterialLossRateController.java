package com.ivo.mrp2.controlller;

import com.ivo.common.result.PageResult;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.service.MaterialLossRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 物料损耗率
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/lossRate")
public class MaterialLossRateController {


    private MaterialLossRateService lossRateService;

    @Autowired
    public MaterialLossRateController(MaterialLossRateService lossRateService) {
        this.lossRateService = lossRateService;
    }

    /**
     * 获取物料损耗率列表
     * @return Result
     */
    @GetMapping("/list")
    public PageResult list() {
        return ResultUtil.successPage(lossRateService.getMaterialLossRate());
    }
}
