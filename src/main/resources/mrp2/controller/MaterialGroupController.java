package com.ivo.mrp2.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.service.MaterialGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 物料组信息接口
 * @author wj
 * @version 1.0
 */
@Api(tags = "物料组信息接口")
@RestController
@RequestMapping("/mrp/materialGroup")
public class MaterialGroupController {

    private MaterialGroupService materialGroupService;

    @Autowired
    public MaterialGroupController(MaterialGroupService materialGroupService) {
        this.materialGroupService = materialGroupService;
    }

    /**
     * 同步物料组数据
     * @return Result
     */
    @ApiOperation("同步物料组数据")
    @GetMapping("/sync")
    public Result syncMaterialGroup() {
        materialGroupService.syncMaterialGroup();
        return ResultUtil.success("同步完成");
    }

    /**
     * 获取物料组
     * @materialGroup 模糊查询物料组
     * @limit 最大条数
     * @return Result
     */
    @ApiOperation("获取物料组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "materialGroup", value = "模糊查询物料组"),
            @ApiImplicitParam(name = "limit", value = "最大条数", defaultValue = "100")
    })
    @GetMapping("/list")
    public Result getMaterialGroup(@RequestParam(defaultValue = "") String materialGroup, @RequestParam(defaultValue = "100") int limit) {
        return ResultUtil.success(materialGroupService.searchMaterialGroup(materialGroup, limit));
    }
}
