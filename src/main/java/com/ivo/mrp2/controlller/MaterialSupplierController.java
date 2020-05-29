package com.ivo.mrp2.controlller;

import com.ivo.common.result.PageResult;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.entity.MaterialSupplier;
import com.ivo.mrp2.service.MaterialSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 材料供应商关联关系维护
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/materialSupplier")
public class MaterialSupplierController {

    private MaterialSupplierService materialSupplierService;

    @Autowired
    public MaterialSupplierController(MaterialSupplierService materialSupplierService) {
        this.materialSupplierService = materialSupplierService;
    }

    /**
     * 分页查询材料供应商的关联
     * @param page 页数
     * @param limit 分页大小
     * @param material 料号
     * @param supplierCode 供应商
     * @return PageResult
     */
    @RequestMapping("/listMaterialSupplier")
    public PageResult listMaterialSupplier(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int limit,
                                           @RequestParam(defaultValue = "") String material,
                                           @RequestParam(defaultValue = "") String supplierCode) {
        Page<MaterialSupplier> materialSupplierPage = materialSupplierService.getPageMaterialSupplier(page, limit, material, supplierCode);
        return ResultUtil.successPage(materialSupplierPage.getContent(), materialSupplierPage.getTotalElements());
    }
}
