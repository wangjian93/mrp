package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.service.MaterialGroupService;
import com.ivo.mrp.service.MaterialService;
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

/**
 * 料号信息
 * @author wj
 * @version 1.0
 */
@Api(tags = "料号信息接口")
@RestController
@RequestMapping("/mrp/material")
public class MaterialController {

    private MaterialService materialService;
    private MaterialGroupService materialGroupService;

    @Autowired
    public MaterialController(MaterialService materialService, MaterialGroupService materialGroupService) {
        this.materialService = materialService;
        this.materialGroupService = materialGroupService;
    }

    @ApiOperation("查询料号信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "search", value = "查询条件")
    })
    @GetMapping("/queryMaterial")
    public Result queryMaterial(@RequestParam(required = false, defaultValue = "1") int page,
                                  @RequestParam(required = false, defaultValue = "50") int limit,
                                  @RequestParam(required = false, defaultValue = "") String search) {
        Page p = materialService.queryMaterial(page-1, limit, search);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    @ApiOperation("查询物料组信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "search", value = "查询条件")
    })
    @GetMapping("/queryMaterialGroup")
    public Result queryMaterialGroup(@RequestParam(required = false, defaultValue = "1") int page,
                                   @RequestParam(required = false, defaultValue = "50") int limit,
                                   @RequestParam(required = false, defaultValue = "") String search) {
        Page p = materialGroupService.queryMaterialGroup(page-1, limit, search);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }
}
