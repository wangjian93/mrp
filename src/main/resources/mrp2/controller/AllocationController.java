package com.ivo.mrp2.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;
import com.ivo.common.utils.DoubleUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.entity.*;
import com.ivo.mrp2.service.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 缺料分配
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/allocation")
public class AllocationController {

    private MrpDataMasterService mrpDataMasterService;
    private MrpService mrpService;
    private MrpDataService mrpDataService;

    private MaterialSupplierService materialSupplierService;

    private MrpAllocationService mrpAllocationService;

    private SupplierService supplierService;

    @Autowired
    public AllocationController(MrpDataMasterService mrpDataMasterService, MrpService mrpService, MrpDataService mrpDataService,
                                MaterialSupplierService materialSupplierService,
                                MrpAllocationService mrpAllocationService,
                                SupplierService supplierService) {
        this.mrpDataMasterService = mrpDataMasterService;
        this.mrpService = mrpService;
        this.mrpDataService = mrpDataService;
        this.materialSupplierService = materialSupplierService;
        this.mrpAllocationService = mrpAllocationService;
        this.supplierService = supplierService;
    }

    /**
     * 获取缺料分配列表
     * @param mrpVer MRP版本
     * @return PageResult
     */
    @RequestMapping("/list")
    public PageResult list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int limit,
                           @RequestParam String mrpVer) {
        MrpVer mrp = mrpService.getMrpVer(mrpVer);
        Page<MrpDataMaster> materialPage = mrpDataMasterService.getPageMrpData(page, limit, mrpVer, "", "", "", "");
        List<MrpDataMaster> mrpDataMasterList = materialPage.getContent();
        List<Map> mapList = new ArrayList<>();
        List<String> materialList = new ArrayList<>();
        Map<String, Integer> cursor = new HashMap<>();   //游标
        for(MrpDataMaster mrpDataMaster : mrpDataMasterList) {
            Map<String, Object> map = new HashMap<>();
            map.put("plant", mrp.getPlant());
            map.put("material", mrpDataMaster.getMaterial());
            map.put("materialName", mrpDataMaster.getMaterialName());
            map.put("products", mrpDataMaster.getProducts());
            map.put("materialGroup", mrpDataMaster.getMaterialGroup());
            mapList.add(map);
            cursor.put(mrpDataMaster.getMaterial(), mapList.size()-1);
            materialList.add(mrpDataMaster.getMaterial());
        }

        List<MrpData> mrpDataList = mrpDataService.getShortMrpData(mrpVer, materialList);
        if(mrpDataList != null && mrpDataList.size()>0) {
            for(MrpData mrpData : mrpDataList) {
                Map<String, Object> map = mapList.get(cursor.get(mrpData.getMaterial()));
                Map<String, Double> subMap = new HashMap<>();
                subMap.put("demandQty", mrpData.getDemandQty());
                subMap.put("lossQty", mrpData.getLossQty());
                subMap.put("arrivalQty", mrpData.getArrivalQty());
                subMap.put("balanceQty", mrpData.getBalanceQty());
                subMap.put("shortQt", mrpData.getShortQty());
                map.put(mrpData.getFabDate().toString(), subMap);
            }
        }
        return ResultUtil.successPage(mapList, materialPage.getTotalElements());
    }

    @ApiOperation("获取材料的分配情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "plant", value = "厂别", required = true),
            @ApiImplicitParam(name = "material", value = "料号", required = true),
            @ApiImplicitParam(name = "fabDate", value = "日期", required = true)
    })
    @GetMapping("/getMaterialAllocation")
    public Result getMrpAllocation(String plant, String material, Date fabDate) {
        List<MrpAllocation> mrpAllocationList = mrpAllocationService.getMrpAllocation(plant, material, fabDate);
        List<Supplier> supplierList = materialSupplierService.getMaterialSupplier(material);
        Set<String> supplierSet = new HashSet<>();
        List<Map> mapList = new ArrayList<>();
        for(MrpAllocation mrpAllocation : mrpAllocationList) {
            supplierSet.add(mrpAllocation.getSupplier());
            Map<String, Object> map = new HashMap<>();
            map.put("supplier", mrpAllocation.getSupplier());
            map.put("supplierName", supplierService.getSupplier(mrpAllocation.getSupplier()).getSName());
            map.put("allocationQty", mrpAllocation.getAllocationQty());
            map.put("allocationDate", mrpAllocation.getFabDate());
            mapList.add(map);
        }
        for(Supplier supplier : supplierList) {
            if(supplierSet.contains(supplier.getId())) continue;
            Map<String, Object> map = new HashMap<>();
            map.put("supplier", supplier.getId());
            map.put("supplierName", supplierService.getSupplier(supplier.getId()).getSName());
            map.put("allocationQty", 0);
            map.put("allocationDate", fabDate);
            mapList.add(map);
        }
        return ResultUtil.success(mapList);
    }

    @ApiOperation("保存材料的缺料分配")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jsons", value = "分配数据JSON格式", required = true),
            @ApiImplicitParam(name = "mrpVer", value = "MRP版本", required = true),
            @ApiImplicitParam(name = "plant", value = "厂别", required = true),
            @ApiImplicitParam(name = "material", value = "料号", required = true),
            @ApiImplicitParam(name = "fabDate", value = "日期", required = true)
    })
    @PostMapping("/saveAllocations")
    public Result saveAllocations(String jsons, String mrpVer, String plant,  String material, Date fabDate) throws IOException, ParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> mapList = objectMapper.readValue(jsons, new TypeReference<List<Map<String, Object>>>(){});
        if(mapList == null || mapList.size() == 0) return null;
        List<MrpAllocation> mrpAllocationList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        double allocationQtyTotal = 0;
        for(Map<String, Object> map : mapList) {
            String supplier = (String) map.get("supplier");
            double allocationQty = Double.parseDouble(map.get("allocationQty").toString() );
            Date allocationDate = new Date((sdf.parse( (String)map.get("allocationDate") )).getTime());
            MrpAllocation mrpAllocation = mrpAllocationService.getMrpAllocation(plant, material, fabDate, supplier);
            if(mrpAllocation == null) {
                if(allocationQty == 0) continue;
                mrpAllocation = new MrpAllocation();
                mrpAllocation.setPlant(plant);
                mrpAllocation.setMaterial(material);
                mrpAllocation.setFabDate(allocationDate);
                mrpAllocation.setAdate(fabDate);
                mrpAllocation.setSupplier(supplier);
                mrpAllocation.setSupplierName(supplierService.getSupplier(supplier).getSName());
                mrpAllocation.setAllocationQty(allocationQty);
                mrpAllocationList.add(mrpAllocation);
                allocationQtyTotal = DoubleUtil.sum(allocationQtyTotal, mrpAllocation.getAllocationQty());
            } else {
                if(mrpAllocation.getAllocationQty() != allocationQty) {
                    mrpAllocation.setAllocationQty(allocationQty);
                    mrpAllocationList.add(mrpAllocation);
                }
                allocationQtyTotal = DoubleUtil.sum(allocationQtyTotal, mrpAllocation.getAllocationQty());
            }
        }
        mrpAllocationService.saveMrpAllocation(mrpAllocationList);
        MrpData mrpData = mrpDataService.getMrpData(mrpVer, material, fabDate);
        mrpData.setAllocationQty(allocationQtyTotal);
        mrpDataService.save(mrpData);
        return ResultUtil.success(allocationQtyTotal);
    }

    /**
     * 获取供应商
     * @param material 料号
     * @return Result
     */
//    @RequestMapping("/getSupplier")
//    public Result getSupplier(String material) {
//        List<MaterialSupplier> list = materialSupplierService.getMaterialSupplier(material);
//        return ResultUtil.success(list);
//    }
}
