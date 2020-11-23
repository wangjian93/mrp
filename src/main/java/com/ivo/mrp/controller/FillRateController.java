package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.entity.MrpVer;
import com.ivo.mrp.service.ActualArrivalService;
import com.ivo.mrp.service.AllocationService;
import com.ivo.mrp.service.ArrivalPlanService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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

    private AllocationService allocationService;

    @Autowired
    public FillRateController(ArrivalPlanService arrivalPlanService, ActualArrivalService actualArrivalService,
                              AllocationService allocationService) {
        this.arrivalPlanService = arrivalPlanService;
        this.actualArrivalService = actualArrivalService;
        this.allocationService = allocationService;
    }


    @GetMapping("/getPageArrivalPlanMaterial")
    public Result getPageArrivalPlanMaterial(Date startDate, Date endDate,
                                             String fab,
                                             int page, int limit,
                                             @RequestParam(required = false, defaultValue = "") String materialGroup,
                                             @RequestParam(required = false, defaultValue = "") String material,
                                             @RequestParam(required = false, defaultValue = "") String supplier) {
        if(StringUtils.equalsAnyIgnoreCase(fab, "LCM")) {
            Page p = arrivalPlanService.getPageLcmArrivalPlanMaterial(startDate, endDate, page-1, limit, materialGroup, material, supplier);
            return ResultUtil.successPage(p.getContent(), p.getTotalElements());
        } else if(StringUtils.equalsAnyIgnoreCase(fab,MrpVer.Type_Cell) ) {
            Page p = arrivalPlanService.getPageCellArrivalPlanMaterial(startDate, endDate, page-1, limit, materialGroup, material, supplier);
            return ResultUtil.successPage(p.getContent(), p.getTotalElements());
        } else if(StringUtils.equalsIgnoreCase(fab, MrpVer.Type_Ary)) {
            Page p = arrivalPlanService.getPageAryArrivalPlanMaterial(startDate, endDate, page-1, limit, materialGroup, material, supplier);
            return ResultUtil.successPage(p.getContent(), p.getTotalElements());
        }
        return null;
    }

    @GetMapping("/getArrivalPlan")
    public Result getArrivalPlan(Date startDate, Date endDate,
                                 String fab,
                                 String material, String supplierCode) {
        List list = new ArrayList();
        if(StringUtils.equalsAnyIgnoreCase(fab, "LCM")) {
            List list1 = arrivalPlanService.getLcmArrivalPlan(startDate, endDate, material, supplierCode);
            List list2 = allocationService.getLcmAllocation(startDate, endDate, material, supplierCode);
            list.addAll(list1);
            list.addAll(list2);
        } else if(StringUtils.equalsAnyIgnoreCase(fab,MrpVer.Type_Cell) ) {
            List list1 = arrivalPlanService.getCellArrivalPlan(startDate, endDate, material, supplierCode);
            List list2 = allocationService.getCellAllocation(startDate, endDate, material, supplierCode);
            list.addAll(list1);
            list.addAll(list2);
        } else if(StringUtils.equalsIgnoreCase(fab, MrpVer.Type_Ary)) {
            List list1 = arrivalPlanService.getAryArrivalPlan(startDate, endDate, material, supplierCode);
            List list2 = allocationService.getAryAllocation(startDate, endDate, material, supplierCode);
            list.addAll(list1);
            list.addAll(list2);
        }
        return ResultUtil.success(list);
    }
}
