package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.entity.direct.lcm.MrpLcmMaterial;
import com.ivo.mrp.entity.lcmPackaging.MrpPackageLcmMaterial;
import com.ivo.mrp.service.DemandPackageLcmService;
import com.ivo.mrp.service.MrpPackageLcmService;
import com.ivo.mrp.service.MrpService;
import com.ivo.mrp.service.RunMrpPackageLcmService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/packageLcm")
public class MrpPackageLcmController {

    private MrpPackageLcmService mrpPackageLcmService;

    private DemandPackageLcmService demandPackageLcmService;

    private RunMrpPackageLcmService runMrpPackageLcmService;

    @Autowired
    public MrpPackageLcmController(MrpPackageLcmService mrpPackageLcmService,
                                   DemandPackageLcmService demandPackageLcmService,
                                   RunMrpPackageLcmService runMrpPackageLcmService) {
        this.mrpPackageLcmService = mrpPackageLcmService;
        this.demandPackageLcmService = demandPackageLcmService;
        this.runMrpPackageLcmService = runMrpPackageLcmService;
    }

    @GetMapping("/getPageMrpPackageLcmMaterial")
    public Result getPageMrpPackageLcmMaterial(@RequestParam(required = false, defaultValue = "1") int page,
                                        @RequestParam(required = false, defaultValue = "50") int limit,
                                        String ver,
                                        @RequestParam(required = false, defaultValue = "") String searchProduct,
                                        @RequestParam(required = false, defaultValue = "") String searchMaterialGroup,
                                        @RequestParam(required = false, defaultValue = "") String searchMaterial,
                                        @RequestParam(required = false, defaultValue = "") String searchSupplier) {
        Page<MrpPackageLcmMaterial> p = mrpPackageLcmService.getPageMrpPackageLcmMaterial(page-1, limit, ver, searchProduct, searchMaterialGroup, searchMaterial, searchSupplier);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }


    @GetMapping("/getMrpPackageLcm")
    public Result getMrpPackageLcm(String ver,  @RequestParam(required = false, defaultValue = "") String product,  String material) {
        Map map = new HashMap<>();
        if(StringUtils.isNotEmpty(product)) {
            List list = mrpPackageLcmService.getMrpPackageLcmProduct(ver, product, material);
            map.put("mrpData", list);
        } else {
            List list =  mrpPackageLcmService.getMrpPackageLcmAlone(ver, material);
            List listDemand = demandPackageLcmService.getDemandQtyAloneDetail(ver, material);
            map.put("demandDate", listDemand);
            map.put("mrpData", list);
        }
        return ResultUtil.success(map);
    }

    @GetMapping("/updateMrpPackageMaterial")
    public Result updateMrpPackageMaterial(String ver, @RequestParam(defaultValue = "", required = false) String product, String material) {
        runMrpPackageLcmService.updateMrpPackageMaterial(ver, product, material);
        return ResultUtil.success("MRP更新料号成功");
    }

    @GetMapping("/updateMrpPackageBalanceQty")
    public Result updateMrpPackageBalanceQty(String ver, @RequestParam(defaultValue = "", required = false) String product, String material, Date fabDate, double balanceQty) {
        runMrpPackageLcmService.updateMrpBalanceQty(ver, product, material, fabDate, balanceQty);
        runMrpPackageLcmService.updateMrpPackageMaterial(ver, product, material);
        return ResultUtil.success("MRP更新料号成功");
    }
}
