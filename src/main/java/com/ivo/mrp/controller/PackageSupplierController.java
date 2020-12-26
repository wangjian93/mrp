package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.entity.packaging.PackageSupplier;
import com.ivo.mrp.repository.packaging.PackageSupplierRepository;
import com.ivo.mrp.service.packageing.PackageSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/packageSupplier")
public class PackageSupplierController {

    private PackageSupplierService packageSupplierService;

    private PackageSupplierRepository packageSupplierRepository;

    @Autowired
    public PackageSupplierController(PackageSupplierService packageSupplierService,
                                     PackageSupplierRepository packageSupplierRepository) {
        this.packageSupplierService = packageSupplierService;
        this.packageSupplierRepository = packageSupplierRepository;
    }

    @RequestMapping("/getSupplier")
    public Result getSupplier(String month, @RequestParam(defaultValue = "", required = false) String searchProduct) {
        List<PackageSupplier> packageSupplierList =  packageSupplierRepository.findByMonthAndProductLike(month, searchProduct+"%");
        List<String> supplierList1 = new ArrayList<>();
        List<String> supplierList2 = new ArrayList<>();
        Map<String, Integer> keyMap = new HashMap<>();
        List<Map> mapList = new ArrayList<>();
        for(PackageSupplier packageSupplier : packageSupplierList) {
            String matrialType = packageSupplier.getMaterialType();
            String supplierCode = packageSupplier.getSupplierCode();
            if(matrialType.equals(PackageSupplier.MaterialType_BOX)) {
                if(!supplierList1.contains(supplierCode)) supplierList1.add(supplierCode);
            } else {
                if(!supplierList2.contains(supplierCode)) supplierList2.add(supplierCode);
            }

            String product = packageSupplier.getProduct();
            String type = packageSupplier.getType();
            String linkQty = packageSupplier.getLinkQty();

            String key = product+type+linkQty;
            Map map;
            if(keyMap.get(key) == null ) {
                map = new HashMap();
                map.put("product", product);
                map.put("type", type);
                map.put("linkQty", linkQty);
                map.put("month", packageSupplier.getMonth());

                mapList.add(map);
                keyMap.put(key, mapList.size()-1);
            } else {
                map = mapList.get(keyMap.get(key));
            }
            map.put(supplierCode, packageSupplier.getAllocationRate());
        }

        Map rMap = new HashMap();
        rMap.put("box", supplierList1);
        rMap.put("tray", supplierList2);
        rMap.put("supplierData", mapList);
        return ResultUtil.success(rMap);
    }
}
