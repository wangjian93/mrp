package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.entity.packaging.MrpPackageMaterial;
import com.ivo.mrp.service.packageing.MrpPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/package")
public class MrpPackageController {

    private MrpPackageService mrpPackageService;

    @Autowired
    public MrpPackageController(MrpPackageService mrpPackageService) {
        this.mrpPackageService = mrpPackageService;
    }

    @GetMapping("/getPageMrpPackageMaterial")
    public Result getPageMrpPackageMaterial(@RequestParam(required = false, defaultValue = "1") int page,
                                        @RequestParam(required = false, defaultValue = "50") int limit,
                                        String ver,
                                        @RequestParam(required = false, defaultValue = "") String searchProduct,
                                        @RequestParam(required = false, defaultValue = "") String searchMaterialGroup,
                                        @RequestParam(required = false, defaultValue = "") String searchMaterial,
                                        @RequestParam(required = false, defaultValue = "") String searchSupplier) {
        Page<MrpPackageMaterial> p = mrpPackageService.getPageMrpPackageMaterial(page-1, limit, ver, searchProduct, searchMaterialGroup, searchMaterial, searchSupplier);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }


    @GetMapping("/getMrpPackage")
    public Result getMrpPackage(String ver, String packageId, String material) {
        List list =  mrpPackageService.getMrpPackage(ver, packageId, material);
        return ResultUtil.success(list);
    }
}
