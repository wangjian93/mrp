package com.ivo.mrp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.entity.Supplier;
import com.ivo.mrp.entity.packaging.*;
import com.ivo.mrp.service.SupplierService;
import com.ivo.mrp.service.packageing.BomPackageService;
import com.ivo.mrp.service.packageing.MrpPackageService;
import com.ivo.mrp.service.packageing.PackageAllocationService;
import com.ivo.mrp.service.packageing.PackageSupplierService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/package")
public class MrpPackageController {

    private MrpPackageService mrpPackageService;

    private PackageAllocationService packageAllocationService;

    private PackageSupplierService packageSupplierService;

    private BomPackageService bomPackageService;

    private SupplierService supplierService;

    @Autowired
    public MrpPackageController(MrpPackageService mrpPackageService, PackageAllocationService packageAllocationService,
                                PackageSupplierService packageSupplierService, BomPackageService bomPackageService,
                                SupplierService supplierService) {
        this.mrpPackageService = mrpPackageService;
        this.packageAllocationService = packageAllocationService;
        this.packageSupplierService = packageSupplierService;
        this.bomPackageService = bomPackageService;
        this.supplierService = supplierService;
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

    @GetMapping("/getAllocationDetail")
    public Result getAllocationDetail(String packageId, String material, Date fabDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String month = sdf.format(fabDate);

        List<PackageAllocation> packageAllocationList = packageAllocationService.getPackageAllocation(packageId, material, fabDate);

        Map<String, Map> map = new HashMap<>();
        for(PackageAllocation packageAllocation : packageAllocationList) {
            String supplierCode = packageAllocation.getSupplierCode();
            Map subMap = new HashMap();
            subMap.put("supplierCode", supplierCode);
            subMap.put("supplierName", packageAllocation.getSupplierName());
            subMap.put("allocationQty", packageAllocation.getAllocationQty());
            map.put(supplierCode, subMap);
        }

        if(StringUtils.isNotEmpty(packageId)) {
            BomPackage bomPackage = bomPackageService.getBomPackageById(packageId);
            BomPackageMaterial bomPackageMaterial = bomPackageService.getBomPackageMaterial(packageId, material);
            String materialName = bomPackageMaterial.getMaterialName();
            String materialType;
            if(StringUtils.containsIgnoreCase(materialName, "箱")) {
                materialType = PackageSupplier.MaterialType_BOX;
            } else {
                materialType = PackageSupplier.MaterialType_TRAY;
            }
            List<PackageSupplier> packageSupplierList = packageSupplierService.getPackageSupplier(month, bomPackage.getProduct(),
                    bomPackage.getType(), bomPackage.getLinkQty(), materialType);
            for(PackageSupplier packageSupplier : packageSupplierList) {
                String supplierCode = packageSupplier.getSupplierCode();
                Map subMap = map.get(supplierCode);
                if(subMap == null) {
                    subMap = new HashMap();
                    subMap.put("supplierCode", supplierCode);
                    subMap.put("supplierName", packageSupplier.getSupplierName());
                    subMap.put("allocationQty", 0);
                    subMap.put("allocationRate", packageSupplier.getAllocationRate());
                    map.put(supplierCode, subMap);
                } else {
                    subMap.put("allocationRate", packageSupplier.getAllocationRate());
                }
            }
        } else {
            List<Supplier> supplierList = supplierService.getSupplierByMaterial(material);
            for(Supplier supplier : supplierList) {
                String supplierCode = supplier.getSupplierCode();
                Map subMap = map.get(supplierCode);
                if(subMap == null) {
                    subMap = new HashMap();
                    subMap.put("supplierCode", supplierCode);
                    subMap.put("supplierName", supplier.getSupplierName());
                    subMap.put("allocationQty", 0);
                    map.put(supplierCode, subMap);
                }
            }
        }

        Collection<Map> valueCollection = map.values();
        List<Map> valueList = new ArrayList<Map>(valueCollection);
        return ResultUtil.successPage(valueList, valueList.size());
    }


    @PostMapping("/batchSaveAllocation")
    public Result batchSaveAllocation(String ver, String packageId, String material, Date fabDate, String jsons) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map> mapList;
        try {
            mapList = objectMapper.readValue(jsons, new TypeReference<List<Map>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.error("JSON数据解析失败，"+e.getMessage());
        }

        BomPackage bomPackage = bomPackageService.getBomPackageById(packageId);
        List<PackageAllocation> packageAllocationList = new ArrayList<>();
        double totalAllocationQty = 0;
        for(Map map : mapList) {
            String supplierCode = (String) map.get("supplierCode");
            String supplierName = (String) map.get("supplierName");
            double allocationQty = Double.parseDouble( String.valueOf(map.get("allocationQty")) );

            PackageAllocation packageAllocation = new PackageAllocation();
            packageAllocation.setPackageId(packageId);
            packageAllocation.setMaterial(material);
            packageAllocation.setFabDate(fabDate);
            packageAllocation.setSupplierCode(supplierCode);
            packageAllocation.setSupplierName(supplierName);
            packageAllocation.setAllocationQty(allocationQty);
            if(bomPackage != null) {
                packageAllocation.setProduct(bomPackage.getProduct());
                packageAllocation.setType(bomPackage.getType());
                packageAllocation.setLinkQty(bomPackage.getLinkQty());
            }

            packageAllocationList.add(packageAllocation);

            totalAllocationQty += allocationQty;
        }

        packageAllocationService.save(packageAllocationList);
        mrpPackageService.updateAllocationQty(ver, packageId, material, fabDate, totalAllocationQty);
        return ResultUtil.success("保存供应商的数量分配成功", totalAllocationQty);
    }
}
