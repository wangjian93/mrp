package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.entity.direct.lcm.MrpLcm;
import com.ivo.mrp.entity.direct.lcm.MrpLcmMaterial;
import com.ivo.mrp.service.MrpService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * MRP数据接口
 * @author wj
 * @version 1.0
 */
@Api(tags = "MRP数据接口")
@RestController
@RequestMapping("/mrp/mrp")
public class MrpController {

    private MrpService mrpService;

    @Autowired
    public MrpController(MrpService mrpService) {
        this.mrpService = mrpService;
    }

    @ApiOperation("查询MRP版本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "fab", value = "查询厂别"),
            @ApiImplicitParam(name = "type", value = "查询类型"),
            @ApiImplicitParam(name = "ver", value = "查询版本")
    })
    @GetMapping("/queryMrpVer")
    public Result queryMrpVer(@RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "50") int limit,
                              @RequestParam(required = false, defaultValue = "") String fab,
                              @RequestParam(required = false, defaultValue = "") String type,
                              @RequestParam(required = false, defaultValue = "") String ver) {
        Page p = mrpService.queryMrpVer(page-1, limit, fab, type, ver);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    @ApiOperation("获取MRP版本数据")
    @ApiImplicitParam(name = "ver", value = "Mps版本", required = true)
    @GetMapping("/getMrpData")
    public Result getMrpData(String ver) {
        List list = mrpService.getMrpDate(ver);
        return ResultUtil.success(list);
    }


    @ApiOperation("分页获取LCM的MRP料号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "ver", value = "MRP版本", required = true),
            @ApiImplicitParam(name = "searchProduct", value = "查询机种"),
            @ApiImplicitParam(name = "searchMaterialGroup", value = "查询物料组"),
            @ApiImplicitParam(name = "searchMaterial", value = "查询料号"),
            @ApiImplicitParam(name = "searchSupplier", value = "查询供应商")
    })
    @GetMapping("/getPageMrpLcmMaterial")
    public Result getPageMrpLcmMaterial(@RequestParam(required = false, defaultValue = "1") int page,
                                        @RequestParam(required = false, defaultValue = "50") int limit,
                                        String ver,
                                        @RequestParam(required = false, defaultValue = "") String searchProduct,
                                        @RequestParam(required = false, defaultValue = "") String searchMaterialGroup,
                                        @RequestParam(required = false, defaultValue = "") String searchMaterial,
                                        @RequestParam(required = false, defaultValue = "") String searchSupplier) {
        Page<MrpLcmMaterial> p = mrpService.getPageMrpLcmMaterial(page-1, limit, ver, searchProduct, searchMaterialGroup, searchMaterial, searchSupplier);
//        List<MrpLcmMaterial> list = p.getContent();
//        for(MrpLcmMaterial mrpLcmMaterial : list) {
//            mrpLcmMaterial.setMrpLcmList(mrpService.getMrpLcm(ver, mrpLcmMaterial.getMaterial()));
//        }
//        List<MrpLcm> mrpLcmList = mrpService.getMrpLcm(ver, materialList);
//        for(MrpLcm mrpLcm : mrpLcmList) {
//            String material = mrpLcm.getMaterial();
//            int index = materialList.indexOf(material);
//            MrpLcmMaterial lcmMaterial = list.get(index);
//            lcmMaterial.getMrpLcmList().add(mrpLcm);
//        }
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    @ApiOperation("获取LCM的MRP数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ver", value = "MRP版本", required = true),
            @ApiImplicitParam(name = "material", value = "料号", required = true)
    })
    @GetMapping("/getMrpLcm")
    public Result getMrpLcm(String ver, String material) {
        List list =  mrpService.getMrpLcm(ver, material);
        return ResultUtil.success(list);
    }
}
