package com.ivo.mrp2.controller;

import com.ivo.common.result.PageResult;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.service.MrpAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/mrpAllocation")
public class MrpAllocationController {

    private MrpAllocationService mrpAllocationService;

    @Autowired
    public MrpAllocationController(MrpAllocationService mrpAllocationService) {
        this.mrpAllocationService = mrpAllocationService;
    }

    @RequestMapping("/list")
    public PageResult list() {
        List list = mrpAllocationService.list();
        return ResultUtil.successPage(list);
    }
}
