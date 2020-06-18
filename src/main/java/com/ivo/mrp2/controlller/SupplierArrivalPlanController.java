package com.ivo.mrp2.controlller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;
import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.DoubleUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.entity.MrpVer;
import com.ivo.mrp2.entity.SupplierArrivalPlan;
import com.ivo.mrp2.service.MaterialSupplierService;
import com.ivo.mrp2.service.MrpService2;
import com.ivo.mrp2.service.SupplierArrivalPlanService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应商到货计划管理
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/supplierArrivalPlan")
public class SupplierArrivalPlanController {


    private SupplierArrivalPlanService supplierArrivalPlanService;

    private MrpService2 mrpService;

    private MaterialSupplierService materialSupplierService;

    @Autowired
    public SupplierArrivalPlanController(SupplierArrivalPlanService supplierArrivalPlanService, MrpService2 mrpService,
                                         MaterialSupplierService materialSupplierService) {
        this.supplierArrivalPlanService = supplierArrivalPlanService;
        this.mrpService = mrpService;
        this.materialSupplierService = materialSupplierService;
    }

    /**
     * 获取供应商到货计划分配列表
     * @param material 料号
     * @param fabDate 日期
     * @param mrpVer MRP版本
     * @return PageResult
     */
    @RequestMapping("/getAllocationList")
    public Result getAllocationList(@RequestParam String material, @RequestParam Date fabDate, @RequestParam String mrpVer) {
        MrpVer mrpVerO = mrpService.getMrpVer(mrpVer);
        List<SupplierArrivalPlan> supplierArrivalPlanList = supplierArrivalPlanService.getSupplierArrivalPlan(mrpVerO.getPlant(), material, fabDate);
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Integer> c = new HashMap<>();
        for(SupplierArrivalPlan supplierArrivalPlan : supplierArrivalPlanList) {
            if(c.get(supplierArrivalPlan.getSupplierCode())==null) {
                Map<String, Object> map = new HashMap<>();
                map.put("material", supplierArrivalPlan.getMaterial());
                map.put("fabDate", supplierArrivalPlan.getDate().toString());
                map.put("supplierCode", supplierArrivalPlan.getSupplierCode());
                map.put("supplier", supplierArrivalPlan.getSupplier());
                map.put("arrivalQty", supplierArrivalPlan.getArrivalQty());
                map.put("allocationQty", 0);
                if(StringUtils.equals(supplierArrivalPlan.getMrpVer(), mrpVer)) {
                    map.put("allocationQty", supplierArrivalPlan.getArrivalQty());
                }
                mapList.add(map);
                c.put(supplierArrivalPlan.getSupplierCode(), mapList.size()-1);
            } else {
                Map<String, Object> map = mapList.get(c.get(supplierArrivalPlan.getSupplierCode()));
                double arrivalQty = (double) map.get("arrivalQty");
                arrivalQty = DoubleUtil.sum(arrivalQty, supplierArrivalPlan.getArrivalQty());
                map.put("arrivalQty", arrivalQty);
                if(StringUtils.equals(supplierArrivalPlan.getMrpVer(), mrpVer)) {
                    map.put("allocationQty", supplierArrivalPlan.getArrivalQty());
                }
            }
        }
        return ResultUtil.success(mapList);
    }

    @RequestMapping("/submit")
    public Result allocation(@RequestParam String mrpVer, @RequestParam String material, @RequestParam Date fabDate, @RequestParam String allocation) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<SupplierArrivalPlan> supplierArrivalPlanList = new ArrayList<>();
        Map[] maps = mapper.readValue(allocation, Map[].class);
        MrpVer mrpVerO = mrpService.getMrpVer(mrpVer);
        for(Map map : maps) {
            String supplier = (String) map.get("supplier");
            Object obj = map.get("allocationQty");
            double allocationQty;
            if(obj instanceof String) {
                allocationQty = Double.valueOf((String) obj);
            } else if(obj instanceof Integer) {
                allocationQty = Double.valueOf((Integer) obj);
            } else {
                allocationQty = (double) obj;
            }

            SupplierArrivalPlan supplierArrivalPlan = new SupplierArrivalPlan();
            supplierArrivalPlan.setMaterial(material);
            supplierArrivalPlan.setDate(fabDate);
            supplierArrivalPlan.setMrpVer(mrpVer);
            supplierArrivalPlan.setSupplierCode(supplier);
            supplierArrivalPlan.setSupplier(materialSupplierService.getSupplerName(supplier));
            supplierArrivalPlan.setArrivalQty(allocationQty);
            supplierArrivalPlan.setPlant(mrpVerO.getPlant());
            supplierArrivalPlanList.add(supplierArrivalPlan);
        }
        supplierArrivalPlanService.saveSupplierArrivalPlan(supplierArrivalPlanList);
        mrpService.updateMrpData(mrpVer, material, fabDate);
        return ResultUtil.success();
    }

    /**
     * 获取开始结束时间的日历
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return Result
     */
    @RequestMapping(value = "/getCalendar")
    public Result getCalendar(@RequestParam Date startDate, @RequestParam Date endDate) {
        List<java.util.Date> list = DateUtil.getCalendar(startDate, endDate);
        List<String> days = DateUtil.format_(list);
        String[] weeks = DateUtil.getWeekDay_(list);
        Map<String, Object> map = new HashMap<>();
        map.put("days", days);
        map.put("weeks", weeks);
        return ResultUtil.success(map);
    }

    /**
     * 分页查询供应商的到货计划
     * @param page 页数
     * @param limit 分页大小
     * @param material 料号
     * @param supplier 供应商
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return PageResult
     */
    @RequestMapping(value = "/listSupplerArrivalPlan")
    public PageResult listSupplerArrivalPlan(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int limit,
                                             @RequestParam(defaultValue = "") String plant,
                                             @RequestParam(defaultValue = "") String material,
                                             @RequestParam(defaultValue = "") String supplier,
                                             @RequestParam Date startDate,
                                             @RequestParam Date endDate) {

        Page<Map> mapPage = supplierArrivalPlanService.pageQuerySupplierArrivalPlan(page, limit, plant,
                material, supplier, startDate, endDate);
        List<Map> mapList = mapPage.getContent();
        List<Map> resultData = new ArrayList<>();
        for(Map map : mapList) {
            Map supMap = new HashMap();
            String material_ = (String) map.get("material");
            String plant_ = (String) map.get("plant");
            String supplierCode_ = (String) map.get("supplierCode");
            supMap.put("material", material_);
            supMap.put("plant", plant_);
            supMap.put("supplierCode", supplierCode_);
            List<Map> arrivalQtyList = supplierArrivalPlanService.getSupplierArrivalQty(startDate, endDate, plant_, material_, supplierCode_);
            for(Map map1 : arrivalQtyList) {
                Date date = (Date) map1.get("date");
                double arrivalQty = (double) map1.get("arrivalQty");
                supMap.put(date.toString(), arrivalQty);
            }
            resultData.add(supMap);
        }
        return ResultUtil.successPage(resultData, mapPage.getTotalElements());
    }
}
