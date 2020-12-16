package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.entity.ActualArrival;
import com.ivo.mrp.entity.Supplier;
import com.ivo.mrp.entity.direct.ArrivalPlan;
import com.ivo.mrp.service.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Api(tags = "供应商达交率")
@RestController
@RequestMapping("/mrp/fillRate")
public class FillRateController {

    private ArrivalPlanService arrivalPlanService;

    private ActualArrivalService actualArrivalService;

    private MaterialService materialService;

    private SupplierService supplierService;

    @Autowired
    public FillRateController(ArrivalPlanService arrivalPlanService, ActualArrivalService actualArrivalService,
                              MaterialService materialService, SupplierService supplierService) {
        this.arrivalPlanService = arrivalPlanService;
        this.actualArrivalService = actualArrivalService;
        this.materialService = materialService;
        this.supplierService = supplierService;
    }

    @GetMapping("/getPageArrivalPlanMaterial")
    public Result getPageArrivalPlanMaterial(Date startDate, Date endDate,
                                             String fab,
                                             int page, int limit,
                                             @RequestParam(required = false, defaultValue = "") String materialGroup,
                                             @RequestParam(required = false, defaultValue = "") String material,
                                             @RequestParam(required = false, defaultValue = "") String supplier) {
        Page p = arrivalPlanService.getPageFillRateMaterial(fab, startDate, endDate, page-1, limit, materialGroup, material, supplier);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    @GetMapping("/getArrivalPlan")
    public Result getArrivalPlan(Date startDate, Date endDate,
                                 String fab,
                                 String material, String supplierCode) {
        List<ArrivalPlan> list1 = arrivalPlanService.getArrivalPlan(fab, startDate, endDate, material, supplierCode);
        List<ActualArrival> list2 = actualArrivalService.getActualArrival(fab, startDate, endDate, material, supplierCode);
        Map<String, Map> dataMap = new HashMap<>();
        for(ArrivalPlan arrivalPlan : list1) {
            String fabDate = arrivalPlan.getFabDate().toString();
            if(dataMap.get(fabDate) == null) {
                Map subMap = new HashMap();
                subMap.put("fabDate", fabDate);
                dataMap.put(fabDate, subMap);
            }
            dataMap.get(fabDate).put("arrivalQty", arrivalPlan.getArrivalQty());
            dataMap.get(fabDate).put("differentQty", arrivalPlan.getArrivalQty());
        }
        for(ActualArrival actualArrival : list2) {
            String fabDate = actualArrival.getFabDate().toString();
            if(dataMap.get(fabDate) == null) {
                Map subMap = new HashMap();
                subMap.put("fabDate", fabDate);
                dataMap.put(fabDate, subMap);
            }
            double arrivalQty = 0d;
            double actualArrivalQty = actualArrival.getQty();
            if(dataMap.get(fabDate).get("arrivalQty") != null) {
                arrivalQty = (double) dataMap.get(fabDate).get("arrivalQty");
            }

            dataMap.get(fabDate).put("actualArrivalQty", actualArrivalQty);
            dataMap.get(fabDate).put("differentQty", arrivalQty-actualArrivalQty);
        }

        List<Map> list = new ArrayList<>(dataMap.values());

        String materialGroup = materialService.getMaterialGroup(material);
        String materialName = materialService.getMaterialName(material);
        Supplier supplier = supplierService.getSupplier(supplierCode);
        Map map = new HashMap();
        map.put("materialGroup",  materialGroup);
        map.put("materialName",  materialName);
        map.put("supplierName",  supplier!=null ? supplier.getSupplierSname() : "");
        map.put("data", list);
        return ResultUtil.success(map);
    }
}
