package com.ivo.mrp2.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.service.MaterialService;
import com.ivo.mrp2.service.MaterialSubstituteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

/**
 * 替代料信息接口
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/materialSubstitute")
@Api(tags = "替代料信息接口")
public class MaterialSubstituteController {

    private MaterialSubstituteService materialSubstituteService;

    private MaterialService materialService;

    @Autowired
    public MaterialSubstituteController(MaterialSubstituteService materialSubstituteService, MaterialService materialService) {
        this.materialSubstituteService = materialSubstituteService;
        this.materialService = materialService;
    }

    /**
     * 同步更新替代料
     * @return Result
     */
    @ApiOperation(value = "同步更新替代料")
    @GetMapping("/syncMaterialSubstitute")
    public Result syncMaterialSubstitute() {
        materialSubstituteService.syncMaterialSubstitute();
        return ResultUtil.success();
    }

    /**
     * 分页查询替代料
     * @param page 页数
     * @param limit 分页大小
     * @param plant 厂别
     * @param product  机种
     * @param materialGroup 物料组
     * @return PageResult
     */
    @ApiOperation(value = "分页查询替代料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "plant", value = "厂别"),
            @ApiImplicitParam(name = "product", value = "机种"),
            @ApiImplicitParam(name = "materialGroup", value = "物料组")
    })
    @GetMapping("/list")
    public PageResult getSubstituteMaterial(@RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "50") int limit,
                                                @RequestParam(defaultValue = "") String plant,
                                                @RequestParam(defaultValue = "") String product,
                                                @RequestParam(defaultValue = "") String materialGroup) {
        Page p = materialSubstituteService.getMaterialSubstitute(page-1, limit, plant, product, materialGroup);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    /**
     * 获取一组替代料
     * @param plant 厂别
     * @param product 机种
     * @param material 料号
     * @return Result
     */
    @GetMapping("/getSubstituteMaterial")
    @ApiOperation(value = "获取一组替代料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "plant", value = "厂别", required = true),
            @ApiImplicitParam(name = "product", value = "机种", required = true),
            @ApiImplicitParam(name = "materialGroup", value = "物料组"),
            @ApiImplicitParam(name = "material", value = "料号"),
    })
    public Result getSubstituteMaterial(String plant, String product,
                                        @RequestParam(defaultValue = "") String materialGroup,
                                        @RequestParam(defaultValue = "") String material) {
        if(StringUtils.isEmpty(materialGroup)) {
            materialGroup = materialService.getMaterialGroup(material);
        }
        return ResultUtil.success(materialSubstituteService.getMaterialSubstitute(plant, product, materialGroup));
    }


    /**
     * 替代料提交
     * @param plant 厂别
     * @param product 机种
     * @param materialGroup 物料组
     * @param substituteMaterialJsons 料号替代比例JSON数组字符串
     * @return Result
     */
    @ApiOperation("替代料提交")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "plant", value = "厂别", required = true),
            @ApiImplicitParam(name = "product", value = "机种", required = true),
            @ApiImplicitParam(name = "materialGroup", value = "物料组", required = true),
            @ApiImplicitParam(name = "substituteMaterialJsons", value = "料号替代比例JSON数组字符串", required = true),
    })
    @PostMapping("/submit")
    public Result submit(String plant, String product, String materialGroup, String substituteMaterialJsons) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Double> map = new HashMap<>();
        try {
            map = mapper.readValue(substituteMaterialJsons, new TypeReference<Map<String, Double>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        materialSubstituteService.saveMaterialSubstitute(plant, product, materialGroup, map);
        return ResultUtil.success();
    }
}
