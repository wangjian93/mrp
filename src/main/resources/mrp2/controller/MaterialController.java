package com.ivo.mrp2.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.service.MaterialService;
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
 * 物料信息接口
 * @author wj
 * @version 1.0
 */
@Api(tags = "物料信息接口")
@RestController
@RequestMapping("/mrp/material")
public class MaterialController {

    private MaterialService materialService;

    @Autowired
    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    /**
     * 同步料号数据
     * @return Result
     */
    @ApiOperation("同步料号数据")
    @GetMapping("/sync")
    public Result syncMaterial() {
        materialService.syncMaterial();
        return ResultUtil.success("同步完成");
    }

    /**
     * 获取料号
     * @param material 模糊查询物料组
     * @limit 最大条数
     * @return Result
     */
    @ApiOperation("获取料号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "material", value = "模糊查询物料组"),
            @ApiImplicitParam(name = "limit", value = "最大条数", defaultValue = "50")
    })
    @GetMapping("/list")
    public Result getMaterial(@RequestParam(defaultValue = "", required = false) String material,
                              @RequestParam(defaultValue = "50") int limit) {
        return ResultUtil.success(materialService.searchMaterial(material, limit));
    }

    /**
     * 获取物料组下的料号
     * @param materialGroup 物料组
     * @return Result
     */
    @ApiOperation("获取物料组下的料号")
    @ApiImplicitParam(name = "materialGroup", value = "物料组", required = true)
    @GetMapping("/getMaterialByGroup")
    public Result getMaterialByGroup(String materialGroup) {
        return ResultUtil.success(materialService.getMaterialByGroup(materialGroup));
    }


    /**
     * 获取材料的物料名
     * @param material 料号
     * @return Result
     */
    @ApiOperation("获取物料组下的料号")
    @ApiImplicitParam(name = "material", value = "料号", required = true)
    @GetMapping("/getMaterialName")
    public Result getMaterialName(String material) {
        return ResultUtil.success("OK",  materialService.getMaterialName(material));
    }

}
