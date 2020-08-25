package com.ivo.mrp2.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.entity.Bom;
import com.ivo.mrp2.service.BomService;
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
 * BOM信息接口
 * @author wj
 * @version 1.0
 */
@Api(tags = "BOM信息接口")
@RestController
@RequestMapping("/mrp/bom")
public class BomController {

    private BomService bomService;

    @Autowired
    public BomController(BomService bomService) {
        this.bomService = bomService;
    }


    /**
     * 同步BOM信息
     * @return Result
     */
    @ApiOperation("同步BOM信息")
    @GetMapping("/sync")
    public Result syncBom() {
        bomService.syncBom();
        return ResultUtil.success("同步完成");
    }

    /**
     * 获取机种的BOM材料清单
     * @param plant 厂别
     * @param product 机种
     * @return Result
     */
    @ApiOperation("获取机种的BOM材料清单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "plant", value = "厂别", required = true),
            @ApiImplicitParam(name = "product", value = "机种", required = true),
            @ApiImplicitParam(name = "materialGroup", value = "物料组", required = true)
    })
    @GetMapping("/getProductBom")
    public PageResult getProductBom(String plant, String product, @RequestParam(required = false) String materialGroup) {
        if(StringUtils.isEmpty(materialGroup)) {
            return ResultUtil.successPage(bomService.getProductBom(plant, product));
        } else {
            return ResultUtil.successPage(bomService.getMaterialGroupBom(plant, product, materialGroup));
        }
    }

    /**
     * 查询机种
     * @param page 页数
     * @param limit 分页大小
     * @param plant 厂别
     * @param product 机种
     * @return Result
     */
    @ApiOperation("查询机种")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "20"),
            @ApiImplicitParam(name = "plant", value = "厂别"),
            @ApiImplicitParam(name = "product", value = "机种")
    })
    @GetMapping("/searchProduct")
    public PageResult getProduct(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue="20") int limit,
                                 @RequestParam(defaultValue = "") String plant,
                                 @RequestParam(defaultValue = "") String product) {
        Page<String> mapPage = bomService.searchProduct(page-1, limit, plant, product);
        List<String> list = mapPage.getContent();
        List<Map> mapList = new ArrayList<>();
        for(String p : list) {
            Map<String, String> map = new HashMap<>();
            map.put("plant", plant);
            map.put("product", p);
            mapList.add(map);
        }
        return ResultUtil.successPage(mapList, mapPage.getTotalElements());
    }

    /**
     * 料号有效性提交
     * @param materialEffectJsons 料号有效性JSON数组字符串
     * @return Result
     */
    @ApiOperation("料号有效性提交")
    @ApiImplicitParam(name = "materialEffectJsons", value = "料号有效性JSON数组字符串", required = true)
    @PostMapping("/submit")
    public Result submit(String materialEffectJsons) {
        ObjectMapper mapper = new ObjectMapper();
        List<Map> mapList;
        List<Bom> bomList = new ArrayList<>();
        try {
            mapList = mapper.readValue(materialEffectJsons, new TypeReference<List<Map>>() {});
            if(mapList != null && mapList.size()>0) {
                for(Map map : mapList) {
                    String plant = (String) map.get("plant");
                    String product = (String) map.get("product");
                    String material = (String) map.get("material");
                    String material_ = (String) map.get("material_");
                    boolean effectFlag = (boolean) map.get("effectFlag");
                    Bom bom = bomService.getBom(plant, product, material, material_);
                    if(effectFlag == bom.isEffectFlag()) continue;
                    bom.setEffectFlag(effectFlag);
                    if(!effectFlag) {
                        bom.setExpireDate(new Date());
                    } else {
                        bom.setExpireDate(null);
                    }
                    bom.setMemo("有效性修改");
                    bomList.add(bom);
                }
            }
            if(bomList.size()>0) {
                bomService.save(bomList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultUtil.success("提供成功，修改了料号"+bomList.size()+"项");
    }

    /**
     * 查询机种
     * @param limit 分页大小
     * @param plant 厂别
     * @param product 机种
     * @return Result
     */
    @ApiOperation("查询机种")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limit", value = "数据最大条数", defaultValue = "200"),
            @ApiImplicitParam(name = "plant", value = "厂别", required = true),
            @ApiImplicitParam(name = "product", value = "机种")
    })
    @GetMapping("/getProduct")
    public PageResult getProduct(
                                 @RequestParam(defaultValue="200") int limit,
                                 @RequestParam String plant,
                                 @RequestParam(defaultValue = "") String product) {
        Page<String> mapPage = bomService.searchProduct(0, limit, plant, product);
        return ResultUtil.successPage(mapPage.getContent());
    }

    /**
     * 获取BOM机种下的物料组
     * @param plant 厂别
     * @param product 机种
     * @return Result
     */
    @ApiOperation("获取BOM机种下的物料组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "plant", value = "厂别", required = true),
            @ApiImplicitParam(name = "product", value = "机种", required = true),
    })
    @GetMapping("/getMaterialGroup")
    public Result getMaterialGroup(String plant, String product) {
        return ResultUtil.success(bomService.getMaterialGroup(plant, product));
    }

    /**
     * 获取BOM物料组下的料号
     * @param plant 厂别
     * @param product 机种
     * @param materialGroup 物料组
     * @return Result
     */
    @ApiOperation("获取BOM物料组下的料号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "plant", value = "厂别", required = true),
            @ApiImplicitParam(name = "product", value = "机种", required = true),
            @ApiImplicitParam(name = "materialGroup", value = "物料组", required = true)
    })
    @GetMapping("/getMaterial")
    public Result getMaterial(String plant, String product, String materialGroup) {
        return ResultUtil.success(bomService.getMaterial(plant, product, materialGroup));
    }
}
