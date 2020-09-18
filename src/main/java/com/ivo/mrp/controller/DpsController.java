package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.service.DpsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * DPS数据接口
 * @author wj
 * @version 1.0
 */
@Api(tags = "DPS数据接口")
@RestController
@RequestMapping("/mrp/dps")
public class DpsController {

    private DpsService dpsService;

    @Autowired
    public DpsController(DpsService dpsService) {
        this.dpsService = dpsService;
    }

    @ApiOperation("查询DPS版本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "fab", value = "查询厂别"),
            @ApiImplicitParam(name = "type", value = "查询类型"),
            @ApiImplicitParam(name = "ver", value = "查询版本")
    })
    @GetMapping("/queryDpsVer")
    public Result queryDpsVer(@RequestParam(required = false, defaultValue = "1") int page,
                                @RequestParam(required = false, defaultValue = "50") int limit,
                                @RequestParam(required = false, defaultValue = "") String fab,
                                @RequestParam(required = false, defaultValue = "") String type,
                                @RequestParam(required = false, defaultValue = "") String ver) {
        Page p = dpsService.queryDpsVer(page-1, limit, fab, type, ver);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    @ApiOperation("获取DPS版本数据")
    @ApiImplicitParam(name = "ver", value = "DPS版本", required = true)
    @GetMapping("/getDpsData")
    public Result getDpsData(String ver) {
        List list = dpsService.getDpsDate(ver);
        return ResultUtil.success(list);
    }
}
