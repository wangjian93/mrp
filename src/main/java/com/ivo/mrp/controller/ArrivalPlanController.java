package com.ivo.mrp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.result.Result;
import com.ivo.common.utils.*;
import com.ivo.core.decryption.DecryptException;
import com.ivo.core.decryption.IVODecryptionUtils;
import com.ivo.mrp.entity.MrpVer;
import com.ivo.mrp.entity.Supplier;
import com.ivo.mrp.entity.direct.Allocation;
import com.ivo.mrp.entity.direct.ArrivalPlan;
import com.ivo.mrp.repository.AllocationRepository;
import com.ivo.mrp.repository.ArrivalPlanRepository;
import com.ivo.mrp.service.AllocationService;
import com.ivo.mrp.service.ArrivalPlanService;
import com.ivo.mrp.service.MrpService;
import com.ivo.mrp.service.SupplierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 供应商到货计划接口
 * @author wj
 * @version 1.0
 */
@Api(tags = "供应商到货计划接口")
@RestController
@RequestMapping("/mrp/arrivalPlan")
public class ArrivalPlanController {

    private MrpService mrpService;

    private ArrivalPlanService arrivalPlanService;

    private SupplierService supplierService;

    private AllocationService allocationService;

    private ArrivalPlanRepository arrivalPlanRepository;

    private AllocationRepository allocationRepository;

    @Autowired
    public ArrivalPlanController(MrpService mrpService, ArrivalPlanService arrivalPlanService, SupplierService supplierService,
                                 AllocationService  allocationService, ArrivalPlanRepository arrivalPlanRepository,
                                 AllocationRepository allocationRepository) {
        this.mrpService = mrpService;
        this.arrivalPlanService = arrivalPlanService;
        this.supplierService =supplierService;
        this.allocationService = allocationService;
        this.arrivalPlanRepository = arrivalPlanRepository;
        this.allocationRepository = allocationRepository;
    }

    @ApiOperation("获取MRP料号的供应商到货计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ver", value = "MRP版本"),
            @ApiImplicitParam(name = "material", value = "料号"),
            @ApiImplicitParam(name = "fabDate", value = "日期")
    })
    @GetMapping("/getArrivalQtyByMaterial")
    public Result getArrivalQtyByMaterial(String ver, String material, Date fabDate) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = mrpVer.getFab();
        List list = arrivalPlanService.getArrivalPlan(fab, material, fabDate);
        return ResultUtil.successPage(list, list.size());
    }

    @ApiOperation("获取MRP料号的供应商到货计划2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ver", value = "MRP版本"),
            @ApiImplicitParam(name = "material", value = "料号"),
            @ApiImplicitParam(name = "fabDate", value = "日期")
    })
    @GetMapping("/getArrivalQtyByMaterial2")
    public Result getArrivalQtyByMaterial2(String ver, String material, Date fabDate) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = mrpVer.getFab();
        List<ArrivalPlan> list = arrivalPlanService.getArrivalPlan(fab, material, fabDate);
        Set<String> supplierSet = new HashSet<>();
        for(ArrivalPlan arrivalPlan : list) {
            supplierSet.add(arrivalPlan.getSupplierCode());
        }

        List<Supplier> supplierList = supplierService.getSupplierByMaterial(material);
        for(Supplier supplier : supplierList) {
            if(supplierSet.contains(supplier.getSupplierCode())) continue;

            ArrivalPlan arrivalPlan = new ArrivalPlan();
            arrivalPlan.setFab(mrpVer.getFab());
            arrivalPlan.setArrivalQty(0);
            arrivalPlan.setSupplierCode(supplier.getSupplierCode());
            arrivalPlan.setSupplierSname(supplier.getSupplierSname());
            arrivalPlan.setMaterial(material);
            arrivalPlan.setFabDate(fabDate);
            list.add(arrivalPlan);
        }
        return ResultUtil.successPage(list, list.size());
    }

    @ApiOperation("批量保存供应商到货计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jsons", value = "JSON数据", required = true)
    })
    @PostMapping("/batchSaveArrivalQty")
    public Result batchSaveArrivalQty(String jsons) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Map> mapList = objectMapper.readValue(jsons, new TypeReference<List<Map>>() {});
            arrivalPlanService.batchSaveArrivalPlan(mapList);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.error("JSON数据解析失败，"+e.getMessage());
        }
        return ResultUtil.success("到货计划保存成功");
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

    @PostMapping("/saveArrivalQty")
    public Result saveArrivalQty(String fab, String material, String supplierCode, Date fabDate, double arrivalQty) {
        arrivalPlanService.saveArrivalPlan(fab, material, supplierCode, fabDate, arrivalQty);
        return ResultUtil.success("到货计划保存成功");
    }


    private  List<Map<String, Object>> getAllocationArrivalPlanData(String fab, String materialGroup, String material, String supplier, Date startDate, Date endDate) {
        List<Map> mapList = new ArrayList<>();
        if(StringUtils.equalsAnyIgnoreCase(fab, "LCM")) {
            Page<Map> p = arrivalPlanService.getPageLcmArrivalPlanMaterial(startDate, endDate, 0, 5000, materialGroup, material, supplier);
            mapList = p.getContent();
        } else if(StringUtils.equalsAnyIgnoreCase(fab,MrpVer.Type_Cell) ) {
            Page<Map> p = arrivalPlanService.getPageCellArrivalPlanMaterial(startDate, endDate, 0, 5000, materialGroup, material, supplier);
            mapList = p.getContent();
        } else if(StringUtils.equalsIgnoreCase(fab, MrpVer.Type_Ary)) {
            Page<Map> p = arrivalPlanService.getPageAryArrivalPlanMaterial(startDate, endDate, 0, 5000, materialGroup, material, supplier);
            mapList = p.getContent();
        }

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Integer> materialSupplerMap = new HashMap<>();
        for(Map map : mapList) {
            String material_ = (String) map.get("material");
            String supplierCode = (String) map.get("supplierCode");
            String suppliersName = (String) map.get("supplierSname");
            String materialName = (String) map.get("materialName");
            String materialGroup_ = (String) map.get("materialGroup");


            List<ArrivalPlan> arrivalPlanList = new ArrayList();
            List<Allocation> allocationList = new ArrayList();
            if(StringUtils.equalsAnyIgnoreCase(fab, "LCM")) {
                List<ArrivalPlan> list1 = arrivalPlanService.getLcmArrivalPlan(startDate, endDate, material_, supplierCode);
                List<Allocation> list2 = allocationService.getLcmAllocation(startDate, endDate, material_, supplierCode);
                arrivalPlanList.addAll(list1);
                allocationList.addAll(list2);
            } else if(StringUtils.equalsAnyIgnoreCase(fab,MrpVer.Type_Cell) ) {
                List<ArrivalPlan> list1 = arrivalPlanService.getCellArrivalPlan(startDate, endDate, material_, supplierCode);
                List<Allocation> list2 = allocationService.getCellAllocation(startDate, endDate, material_, supplierCode);
                arrivalPlanList.addAll(list1);
                allocationList.addAll(list2);
            } else if(StringUtils.equalsIgnoreCase(fab, MrpVer.Type_Ary)) {
                List<ArrivalPlan> list1 = arrivalPlanService.getAryArrivalPlan(startDate, endDate, material_, supplierCode);
                List<Allocation> list2 = allocationService.getAryAllocation(startDate, endDate, material_, supplierCode);
                arrivalPlanList.addAll(list1);
                allocationList.addAll(list2);
            }

            for(ArrivalPlan arrivalPlan : arrivalPlanList) {
                Double arrivalQty = arrivalPlan.getArrivalQty();
                String key = material_+supplierCode;
                Date fabDate = arrivalPlan.getFabDate();

                Integer k = materialSupplerMap.get(key);
                Map<String, Object> subMap = null;
                if(k!=null) {
                    subMap = list.get(k);
                }
                if(subMap == null) {
                    subMap = new HashMap<>();
                    subMap.put("material", material_);
                    subMap.put("supplier", supplierCode);
                    subMap.put("materialName", materialName);
                    subMap.put("materialGroup", materialGroup);
                    subMap.put("supplierName", suppliersName);
                    if(StringUtils.containsIgnoreCase(fab, "LCM")) {
                        subMap.put("plant", "LCM");
                    } else {
                        subMap.put("plant", fab);
                    }
                    list.add(subMap);
                    materialSupplerMap.put(key, list.size()-1);
                }
                // LCM1/LCM2需要一起显示
                Map<String, Map> fabDateMap = (Map<String, Map>) subMap.get(fabDate.toString());
                if(fabDateMap == null) {
                    fabDateMap = new HashMap<>();
                    subMap.put(fabDate.toString(), fabDateMap);
                }
                Map<String, Double> qtyMap = new HashMap<>();
                qtyMap.put("arrivalQty", arrivalQty);
                fabDateMap.put(arrivalPlan.getFab(), qtyMap);
            }
            for(Allocation allocation : allocationList) {
                Double allocationQty = allocation.getAllocationQty();
                String key = material_+supplierCode;
                Date fabDate = allocation.getFabDate();

                Integer k = materialSupplerMap.get(key);
                Map<String, Object> subMap = null;
                if(k!=null) {
                    subMap = list.get(k);
                }
                if(subMap == null) {
                    subMap = new HashMap<>();
                    subMap.put("material", material_);
                    subMap.put("supplier", supplierCode);
                    subMap.put("materialName", materialName);
                    subMap.put("materialGroup", materialGroup);
                    subMap.put("supplierName", suppliersName);
                    if(StringUtils.containsIgnoreCase(fab, "LCM")) {
                        subMap.put("plant", "LCM");
                    } else {
                        subMap.put("plant", fab);
                    }
                    list.add(subMap);
                    materialSupplerMap.put(key, list.size()-1);
                }
                // LCM1/LCM2需要一起显示
                Map<String, Map> fabDateMap = (Map<String, Map>) subMap.get(fabDate.toString());
                if(fabDateMap == null) {
                    fabDateMap = new HashMap<>();
                    subMap.put(fabDate.toString(), fabDateMap);
                }
                Map<String, Double> qtyMap = new HashMap<>();
                qtyMap.put("allocationQty", allocationQty);
                fabDateMap.put(allocation.getFab(), qtyMap);
            }
        }
        return list;
    }


    @ApiOperation("供应商到货计划数据导出")
    @GetMapping("/exportArrivalPlan")
    public void exportArrivalPlan(Date startDate, Date endDate, String fab) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        List<java.util.Date> dateList = DateUtil.getCalendar(startDate, endDate);
        String[] weeks = DateUtil.getWeekDay_(dateList);
        String[] titleItems = new String[] {"机种","供应商ID", "供应商", "厂别","料号","品名"};
        List<String> titleList = new ArrayList<>();
        int intRow =0;
        int intCel = 0;
        Row row0 = sheet.createRow(intRow);
        for(; intCel<titleItems.length; intCel++) {
            row0.createCell(intCel).setCellValue(titleItems[intCel]);
            titleList.add(titleItems[intCel]);
        }
        row0.createCell(intCel);
        intCel++;
        titleList.add("");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdf_ = new SimpleDateFormat("yyyy-MM-dd");
        for(java.util.Date date : dateList) {
            row0.createCell(intCel).setCellValue(sdf.format(date));
            intCel++;
            titleList.add(sdf_.format(date));
        }
        intRow++;
        intCel=0;
        Row row1 = sheet.createRow(intRow);
        for(; intCel<titleItems.length; intCel++) {
            row1.createCell(intCel);
        }
        row0.createCell(intCel);
        intCel++;
        for(int i=0; i<dateList.size(); i++) {
            row1.createCell(intCel).setCellValue(weeks[i]);
            intCel++;
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 6, 6));


        List<Map<String, Object>> list = getAllocationArrivalPlanData( fab,  "",  "",  "",  startDate,  endDate);
        for(int i=0; i<list.size(); i++) {
            intRow++;
            intCel=0;
            Map map = list.get(i);
            Row row_1 = sheet.createRow(intRow);
            row_1.createCell(intCel).setCellValue("");
            intCel++;
            row_1.createCell(intCel).setCellValue((String)map.get("supplier"));
            intCel++;
            row_1.createCell(intCel).setCellValue((String)map.get("supplierName"));
            intCel++;
            row_1.createCell(intCel).setCellValue((String)map.get("plant"));
            intCel++;
            row_1.createCell(intCel).setCellValue((String)map.get("material"));
            intCel++;
            row_1.createCell(intCel).setCellValue((String)map.get("materialName"));
            intCel++;
            if(StringUtils.equals(fab, "LCM")) {
                row_1.createCell(intCel).setCellValue("LCM1(要望)");
                intCel++;
                for(; intCel<titleList.size(); intCel++) {
                    Map fabDateMap = (Map) map.get(titleList.get(intCel) );
                    Map qtyLcm1Map = null;
                    Map qtyLcm2Map = null;
                    if(fabDateMap != null) {
                        qtyLcm1Map = (Map)fabDateMap.get("LCM1");
                        qtyLcm2Map = (Map)fabDateMap.get("LCM2");
                    }
                    Double allocationQty = null;
                    if(qtyLcm1Map != null) {
                        if(qtyLcm1Map.get("allocationQty") != null) {
                            allocationQty = (double) qtyLcm1Map.get("allocationQty");
                        }
                    }
                    if(allocationQty == null || allocationQty == 0) {
                        row_1.createCell(intCel).setCellValue("");
                    } else {
                        row_1.createCell(intCel).setCellValue(allocationQty);
                    }
                }

                intRow++;
                intCel = 0;
                Row row_2 = sheet.createRow(intRow);
                row_2.createCell(intCel);
                intCel++;
                row_2.createCell(intCel);
                intCel++;
                row_2.createCell(intCel);
                intCel++;
                row_2.createCell(intCel);
                intCel++;
                row_2.createCell(intCel);
                intCel++;
                row_2.createCell(intCel);
                intCel++;
                row_2.createCell(intCel).setCellValue("LCM2(要望)");
                intCel++;
                for(; intCel<titleList.size(); intCel++) {
                    Map fabDateMap = (Map) map.get(titleList.get(intCel) );
                    Map qtyLcm1Map = null;
                    Map qtyLcm2Map = null;
                    if(fabDateMap != null) {
                        qtyLcm1Map = (Map)fabDateMap.get("LCM1");
                        qtyLcm2Map = (Map)fabDateMap.get("LCM2");
                    }
                    Double allocationQty = null;
                    if(qtyLcm2Map != null) {
                        if(qtyLcm2Map.get("allocationQty") != null) {
                            allocationQty = (double) qtyLcm2Map.get("allocationQty");
                        }
                    }
                    if(allocationQty == null || allocationQty == 0) {
                        row_2.createCell(intCel).setCellValue("");
                    } else {
                        row_2.createCell(intCel).setCellValue(allocationQty);
                    }
                }

                intRow++;
                intCel = 0;
                Row row_3 = sheet.createRow(intRow);
                row_3.createCell(intCel);
                intCel++;
                row_3.createCell(intCel);
                intCel++;
                row_3.createCell(intCel);
                intCel++;
                row_3.createCell(intCel);
                intCel++;
                row_3.createCell(intCel);
                intCel++;
                row_3.createCell(intCel);
                intCel++;
                row_3.createCell(intCel).setCellValue("回复LCM1");
                intCel++;
                for(; intCel<titleList.size(); intCel++) {
                    Map fabDateMap = (Map) map.get(titleList.get(intCel) );
                    Map qtyLcm1Map = null;
                    Map qtyLcm2Map = null;
                    if(fabDateMap != null) {
                        qtyLcm1Map = (Map)fabDateMap.get("LCM1");
                        qtyLcm2Map = (Map)fabDateMap.get("LCM2");
                    }
                    Double arrivalQty = null;
                    if(qtyLcm1Map != null) {
                        if(qtyLcm1Map.get("arrivalQty") != null) {
                            arrivalQty = (double) qtyLcm1Map.get("arrivalQty");
                        }
                    }
                    if(arrivalQty == null || arrivalQty == 0) {
                        row_3.createCell(intCel).setCellValue("");
                    } else {
                        row_3.createCell(intCel).setCellValue(arrivalQty);
                    }
                }

                intRow++;
                intCel = 0;
                Row row_4 = sheet.createRow(intRow);
                row_4.createCell(intCel);
                intCel++;
                row_4.createCell(intCel);
                intCel++;
                row_4.createCell(intCel);
                intCel++;
                row_4.createCell(intCel);
                intCel++;
                row_4.createCell(intCel);
                intCel++;
                row_4.createCell(intCel);
                intCel++;
                row_4.createCell(intCel).setCellValue("回复LCM2");
                intCel++;
                for(; intCel<titleList.size(); intCel++) {
                    Map fabDateMap = (Map) map.get(titleList.get(intCel) );
                    Map qtyLcm1Map = null;
                    Map qtyLcm2Map = null;
                    if(fabDateMap != null) {
                        qtyLcm1Map = (Map)fabDateMap.get("LCM1");
                        qtyLcm2Map = (Map)fabDateMap.get("LCM2");
                    }
                    Double arrivalQty = null;
                    if(qtyLcm2Map != null) {
                        if(qtyLcm2Map.get("arrivalQty") != null) {
                            arrivalQty = (double) qtyLcm2Map.get("arrivalQty");
                        }
                    }
                    if(arrivalQty == null || arrivalQty == 0) {
                        row_4.createCell(intCel).setCellValue("");
                    } else {
                        row_4.createCell(intCel).setCellValue(arrivalQty);
                    }
                }


                sheet.addMergedRegion(new CellRangeAddress(intRow-3, intRow, 0, 0));
                sheet.addMergedRegion(new CellRangeAddress(intRow-3, intRow, 1, 1));
                sheet.addMergedRegion(new CellRangeAddress(intRow-3, intRow, 2, 2));
                sheet.addMergedRegion(new CellRangeAddress(intRow-3, intRow, 3, 3));
                sheet.addMergedRegion(new CellRangeAddress(intRow-3, intRow, 4, 4));
                sheet.addMergedRegion(new CellRangeAddress(intRow-3, intRow, 5, 5));

            } else if(StringUtils.equals(fab, "CELL")) {
                row_1.createCell(intCel).setCellValue("CELL(要望)");
                intCel++;
                for(; intCel<titleList.size(); intCel++) {
                    Map fabDateMap = (Map) map.get(titleList.get(intCel) );
                    Map qtyCellMap = null;
                    if(fabDateMap != null) {
                        qtyCellMap = (Map)fabDateMap.get("CELL");
                    }
                    Double allocationQty = null;
                    if(qtyCellMap != null) {
                        if(qtyCellMap.get("allocationQty") != null) {
                            allocationQty = (double) qtyCellMap.get("allocationQty");
                        }
                    }
                    if(allocationQty == null || allocationQty == 0) {
                        row_1.createCell(intCel).setCellValue("");
                    } else {
                        row_1.createCell(intCel).setCellValue(allocationQty);
                    }
                }

                intRow++;
                intCel = 0;
                Row row_2 = sheet.createRow(intRow);
                row_2.createCell(intCel);
                intCel++;
                row_2.createCell(intCel);
                intCel++;
                row_2.createCell(intCel);
                intCel++;
                row_2.createCell(intCel);
                intCel++;
                row_2.createCell(intCel);
                intCel++;
                row_2.createCell(intCel);
                intCel++;
                row_2.createCell(intCel).setCellValue("回复CELL");
                intCel++;
                for(; intCel<titleList.size(); intCel++) {
                    Map fabDateMap = (Map) map.get(titleList.get(intCel) );
                    Map qtyCellMap = null;
                    if(fabDateMap != null) {
                        qtyCellMap = (Map)fabDateMap.get("CELL");
                    }
                    Double arrivalQty = null;
                    if(qtyCellMap != null) {
                        if(qtyCellMap.get("arrivalQty") != null) {
                            arrivalQty = (double) qtyCellMap.get("arrivalQty");
                        }
                    }
                    if(arrivalQty == null || arrivalQty == 0) {
                        row_2.createCell(intCel).setCellValue("");
                    } else {
                        row_2.createCell(intCel).setCellValue(arrivalQty);
                    }
                }

                sheet.addMergedRegion(new CellRangeAddress(intRow-1, intRow, 0, 0));
                sheet.addMergedRegion(new CellRangeAddress(intRow-1, intRow, 1, 1));
                sheet.addMergedRegion(new CellRangeAddress(intRow-1, intRow, 2, 2));
                sheet.addMergedRegion(new CellRangeAddress(intRow-1, intRow, 3, 3));
                sheet.addMergedRegion(new CellRangeAddress(intRow-1, intRow, 4, 4));
                sheet.addMergedRegion(new CellRangeAddress(intRow-1, intRow, 5, 5));

            } else if(StringUtils.equals(fab, "ARY")) {
                row_1.createCell(intCel).setCellValue("ARY(要望)");
                intCel++;
                for(; intCel<titleList.size(); intCel++) {
                    Map fabDateMap = (Map) map.get(titleList.get(intCel) );
                    Map qtyAryMap = null;
                    if(fabDateMap != null) {
                        qtyAryMap = (Map)fabDateMap.get("ARY");
                    }
                    Double allocationQty = null;
                    if(qtyAryMap != null) {
                        if(qtyAryMap.get("allocationQty") != null) {
                            allocationQty = (double) qtyAryMap.get("allocationQty");
                        }
                    }
                    if(allocationQty == null || allocationQty == 0) {
                        row_1.createCell(intCel).setCellValue("");
                    } else {
                        row_1.createCell(intCel).setCellValue(allocationQty);
                    }
                }

                intRow++;
                intCel = 0;
                Row row_2 = sheet.createRow(intRow);
                row_2.createCell(intCel);
                intCel++;
                row_2.createCell(intCel);
                intCel++;
                row_2.createCell(intCel);
                intCel++;
                row_2.createCell(intCel);
                intCel++;
                row_2.createCell(intCel);
                intCel++;
                row_2.createCell(intCel);
                intCel++;
                row_2.createCell(intCel).setCellValue("回复ARY");
                intCel++;
                for(; intCel<titleList.size(); intCel++) {
                    Map fabDateMap = (Map) map.get(titleList.get(intCel) );
                    Map qtyAryMap = null;
                    if(fabDateMap != null) {
                        qtyAryMap = (Map)fabDateMap.get("ARY");
                    }
                    Double arrivalQty = null;
                    if(qtyAryMap != null) {
                        if(qtyAryMap.get("arrivalQty") != null) {
                            arrivalQty = (double) qtyAryMap.get("arrivalQty");
                        }
                    }
                    if(arrivalQty == null || arrivalQty == 0) {
                        row_2.createCell(intCel).setCellValue("");
                    } else {
                        row_2.createCell(intCel).setCellValue(arrivalQty);
                    }
                }

                sheet.addMergedRegion(new CellRangeAddress(intRow-1, intRow, 0, 0));
                sheet.addMergedRegion(new CellRangeAddress(intRow-1, intRow, 1, 1));
                sheet.addMergedRegion(new CellRangeAddress(intRow-1, intRow, 2, 2));
                sheet.addMergedRegion(new CellRangeAddress(intRow-1, intRow, 3, 3));
                sheet.addMergedRegion(new CellRangeAddress(intRow-1, intRow, 4, 4));
                sheet.addMergedRegion(new CellRangeAddress(intRow-1, intRow, 5, 5));

            }
        }

        HttpServletResponse response = HttpServletUtil.getResponse();
        response.setContentType("application/vnd.ms-excel;chartset=utf-8");
        String fileName = "供应商交期模板";
        try {
            fileName = URLEncoder.encode(fileName, "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-Disposition", "attachment;filename="+fileName + ".xlsx");
        try {
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("Excel上传供应商到货计划")
    @ApiImplicitParam(name="file", value = "上传的excel", required = true)
    @PostMapping(value = "/importArrivalPlan", headers = "content-type=multipart/form-data")
    public Result importArrivalPlan(@RequestParam("file") MultipartFile file) {
        try {
            //IVO文件解密
            byte[] bytes = IVODecryptionUtils.decrypt(file.getInputStream());
            InputStream inputStream = new ByteArrayInputStream(bytes);
            String fileName = file.getOriginalFilename();
            Workbook workbook;
            if (fileName.endsWith("xls")) {
                workbook = new HSSFWorkbook(inputStream);
            }
            else if (fileName.endsWith("xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            }
            else {
                throw new IOException("文件类型错误");
            }
            Sheet sheet = workbook.getSheetAt(0);

            List<Object> titleList = new ArrayList<>();
            List<Date> dateList = new ArrayList<>();

            Row row0 = sheet.getRow(0);
            for(int i=0; i<row0.getPhysicalNumberOfCells(); i++) {
                Cell cell = row0.getCell(i);
                titleList.add(ExcelUtil.getValue(cell));
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            for(int i=0; i<titleList.size(); i++) {
                if(i<7) {
                    dateList.add(null);
                    continue;
                }
                Object object = titleList.get(i);
                if(object instanceof String) {
                    java.util.Date date = sdf.parse((String)object);
                    dateList.add(new Date(date.getTime()));
                } else if(object instanceof java.util.Date) {
                    java.util.Date date = (java.util.Date) object;
                    dateList.add(new Date(date.getTime()));
                } else if(object instanceof BigDecimal) {
                    Calendar calendar = new GregorianCalendar(1900,0,-1);
                    java.util.Date d = calendar.getTime();
                    java.util.Date dd = DateUtils.addDays(d, Integer.valueOf( ((BigDecimal)object).toString() ));
                    dateList.add(new Date(dd.getTime()));
                }
            }
            dateList.size();


            String[] titles = new String[7];
            List<ArrivalPlan> supplierArrivalPlanList_1 = new ArrayList<>();
            List<ArrivalPlan> supplierArrivalPlanList_2 = new ArrayList<>();
            List<Allocation> allocationList_1 = new ArrayList<>();
            List<Allocation> allocationList_2 = new ArrayList<>();
            System.out.println(sheet.getLastRowNum());
            for(int i=2; i<=sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                for(int j=0; j<7; j++) {
                    Cell cell = row.getCell(j);
                    if(cell == null) continue;
                    if(j<7) {
                        Object value;
                        switch (cell.getCellType()) {
                            case BOOLEAN:
                                value = cell.getBooleanCellValue();
                                break;
                            case NUMERIC:
                                value = (int) cell.getNumericCellValue();
                                value = String.valueOf(value);
                                break;
                            default:
                                value = cell.getStringCellValue();
                                break;
                        }
                        if(value != null && !value.equals("")) {
                            titles[j] = (String) value;
                        }
                    }
                }
                System.out.println(titles);
                for(int j=7; j<row0.getPhysicalNumberOfCells(); j++) {
                    Cell cell = row.getCell(j);
                    Object object = ExcelUtil.getValue(cell);
                    Double qty = null;
                    if (object != null) {
                        if (object instanceof String) {
                            if (StringUtils.isNotEmpty((String) object)) {
                                qty = Double.parseDouble((String) object);
                            }
                        } else if (object instanceof BigDecimal) {
                            qty = ((BigDecimal) object).doubleValue();
                        } else if (object instanceof Number) {
                            qty = ((Number) object).doubleValue();
                        }
                    }

                    String supplier = titles[1];
                    String material = titles[4];
                    Date fabDate = dateList.get(j);
                    String plant = null;
                    if (StringUtils.containsIgnoreCase(titles[6], "LCM1")) {
                        plant = "LCM1";
                    } else if (StringUtils.containsIgnoreCase(titles[6], "LCM2")) {
                        plant = "LCM2";
                    } else if (StringUtils.containsIgnoreCase(titles[6], "CELL")) {
                        plant = "CELL";
                    } else if (StringUtils.containsIgnoreCase(titles[6], "ARY")) {
                        plant = "ARY";
                    }

                    if(StringUtils.containsIgnoreCase(titles[6], "要望")) {
                        Allocation allocation = new Allocation();
                        allocation.setFab(plant);
                        allocation.setSupplierCode(supplier);
                        allocation.setMaterial(material);
                        allocation.setFabDate(fabDate);
                        if (qty == null) qty=0d;
                        allocation.setAllocationQty(qty);
                        allocation.setCreateDate(new java.util.Date());

                        if(allocation.getAllocationQty() == 0) {
                            allocationList_2.add(allocation);
                        } else {
                            allocationList_1.add(allocation);
                        }
                    } else {
                        ArrivalPlan supplierArrivalPlan = new ArrivalPlan();
                        supplierArrivalPlan.setFab(plant);
                        supplierArrivalPlan.setSupplierCode(supplier);
                        supplierArrivalPlan.setMaterial(material);
                        supplierArrivalPlan.setFabDate(fabDate);
                        if (qty == null) qty=0d;
                        supplierArrivalPlan.setArrivalQty(qty);
                        supplierArrivalPlan.setCreateDate(new java.util.Date());

                        if(supplierArrivalPlan.getArrivalQty() == 0) {
                            supplierArrivalPlanList_2.add(supplierArrivalPlan);
                        } else {
                            supplierArrivalPlanList_1.add(supplierArrivalPlan);
                        }
                    }
                }
            }
            arrivalPlanRepository.deleteAll(supplierArrivalPlanList_2);
            arrivalPlanRepository.saveAll(supplierArrivalPlanList_1);
            allocationRepository.deleteAll(allocationList_2);
            allocationRepository.saveAll(allocationList_1);
            supplierArrivalPlanList_1.size();
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.error("上传失败：" + e.getMessage());
        } catch (DecryptException e) {
            e.printStackTrace();
            return ResultUtil.error("上传失败");
        } catch (ParseException e) {
            e.printStackTrace();
            return ResultUtil.error("上传失败");
        }
        return ResultUtil.success();
    }

}
