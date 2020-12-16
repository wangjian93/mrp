package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.entity.packaging.BomPackage;
import com.ivo.mrp.service.packageing.BomPackageService;
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
@RequestMapping("/mrp/bomPackage")
public class BomPackageController {

    private BomPackageService bomPackageService;

    @Autowired
    public BomPackageController(BomPackageService bomPackageService) {
        this.bomPackageService = bomPackageService;
    }

    @GetMapping("/queryBomPackage")
    public Result queryBomPackage(@RequestParam(required = false, defaultValue = "1") int page,
                                  @RequestParam(required = false, defaultValue = "50") int limit,
                                  @RequestParam(required = false, defaultValue = "") String searchProduct) {

        Page p = bomPackageService.queryBomPackage(page-1, limit, searchProduct);
        List<BomPackage> bomPackageList = p.getContent();
        for(BomPackage bomPackage : bomPackageList) {
            bomPackage.setBomPackageMaterialList(bomPackageService.getBomPackageMaterial(bomPackage.getId()));
        }
        return ResultUtil.successPage(bomPackageList, p.getTotalElements());
    }
}
