package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.entity.MrpVer;
import com.ivo.mrp.entity.lcmPackaging.MrpPackageLcmMaterial;
import com.ivo.mrp.entity.pol.DpsPol;
import com.ivo.mrp.entity.pol.MrpPol;
import com.ivo.mrp.entity.pol.MrpPolMaterial;
import com.ivo.mrp.service.MrpService;
import com.ivo.mrp.service.pol.DpsPolService;
import com.ivo.mrp.service.pol.MrpPolService;
import com.ivo.mrp.service.pol.RunMrpPolService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/pol")
public class MrpPolController {

    private MrpPolService polService;

    private MrpService mrpService;

    private DpsPolService dpsPolService;

    private RunMrpPolService runMrpPolService;

    @Autowired
    public MrpPolController(MrpPolService polService, MrpService mrpService, DpsPolService dpsPolService,
                            RunMrpPolService runMrpPolService) {
        this.polService = polService;
        this.mrpService = mrpService;
        this.dpsPolService = dpsPolService;
        this.runMrpPolService = runMrpPolService;
    }

    @GetMapping("/getPageMrpPolMatrial")
    public Result getPageMrpPolMatrial(@RequestParam(required = false, defaultValue = "1") int page,
                                               @RequestParam(required = false, defaultValue = "50") int limit,
                                               String ver,
                                               @RequestParam(required = false, defaultValue = "") String searchProduct,
                                               @RequestParam(required = false, defaultValue = "") String searchMaterialGroup,
                                               @RequestParam(required = false, defaultValue = "") String searchMaterial,
                                               @RequestParam(required = false, defaultValue = "") String searchSupplier) {
        Page<MrpPolMaterial> p = polService.getPageMrpPol(page-1, limit, ver, searchProduct, searchMaterialGroup, searchMaterial, searchSupplier);
        List<MrpPolMaterial> mrpPolMaterialList = p.getContent();
        List<MrpPolMaterial> list = new ArrayList<>();
        List<String>  productList = new ArrayList<>();
        for(MrpPolMaterial mrpPolMaterial : mrpPolMaterialList) {
            if(!productList.contains(mrpPolMaterial.getProduct())) {
                 MrpPolMaterial polM = new MrpPolMaterial();
                 polM.setProduct(mrpPolMaterial.getProduct());
                 list.add(polM);
                 productList.add(mrpPolMaterial.getProduct());
             }
             list.add(mrpPolMaterial);
        }
        return ResultUtil.successPage(list, p.getTotalElements());
    }


    @GetMapping("/getMrpPol")
    public Result getMrpPol(String ver,  String product,  String material) {
        List<MrpPol> mrpPolList = polService.getMrpPol(ver, product, material);
        return ResultUtil.success(mrpPolList);
    }

    @GetMapping("/getPolProductDps")
    public Result getPolProductDps(String ver, String product) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String[] dpsVers = mrpService.convertStringToAry(mrpVer.getDpsVer());
        List<DpsPol> dpsPolList = new ArrayList<>();
        for(String dpsVer : dpsVers) {
            dpsPolList.addAll(dpsPolService.getDpsPol(dpsVer, product));
        }
        return ResultUtil.success(dpsPolList);
    }

    @GetMapping("/updateMrp")
    public Result updateMrp(String ver) {
//        runMrpPackageLcmService.up(ver);
        return ResultUtil.success(ver+"更新成功");
    }

    @ApiOperation("选择DPS/MPS版本运算MRP")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dpsVer", value = "DPS版本", required = true)
    })
    @PostMapping("/runMrp")
    public Result runMrp(String dpsVer) throws IOException, ClassNotFoundException {
        String[] dpsVers = new String[]{};
        if(StringUtils.isNotEmpty(dpsVer)) {
            dpsVers = dpsVer.split(",");
        }
        runMrpPolService.runMrpPol(dpsVers, "SYS");
        return ResultUtil.success();
    }

}
