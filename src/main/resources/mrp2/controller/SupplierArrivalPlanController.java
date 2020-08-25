package com.ivo.mrp2.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;
import com.ivo.common.utils.*;
import com.ivo.common.utils.DateUtil;
import com.ivo.core.decryption.DecryptException;
import com.ivo.core.decryption.IVODecryptionUtils;
import com.ivo.mrp2.entity.Supplier;
import com.ivo.mrp2.entity.SupplierArrivalPlan;
import com.ivo.mrp2.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Id;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 供应商到货计划管理
 * @author wj
 * @version 1.0
 */
@Api(tags = "供应商到货计划接口")
@RestController
@RequestMapping("/mrp/supplierArrivalPlan")
public class SupplierArrivalPlanController {


    private SupplierArrivalPlanService supplierArrivalPlanService;

    private MrpService mrpService;

    private MaterialSupplierService materialSupplierService;


    private MaterialService materialService;

    private SupplierService supplierService;

    private MrpAllocationService mrpAllocationService;

    @Autowired
    public SupplierArrivalPlanController(SupplierArrivalPlanService supplierArrivalPlanService, MrpService mrpService,
                                         MaterialSupplierService materialSupplierService,
                                         MaterialService materialService,SupplierService supplierService,
                                         MrpAllocationService mrpAllocationService) {
        this.supplierArrivalPlanService = supplierArrivalPlanService;
        this.mrpService = mrpService;
        this.materialSupplierService = materialSupplierService;
        this.materialService = materialService;
        this.supplierService = supplierService;
        this.mrpAllocationService = mrpAllocationService;
    }

    /**
     *
     * @param material 料号
     * @param supplier 供应商
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return PageResult
     */
    @ApiOperation("获取供应商的到货计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = true),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = true),
            @ApiImplicitParam(name = "plant", value = "厂别"),
            @ApiImplicitParam(name = "material", value = "料号"),
            @ApiImplicitParam(name = "supplier", value = "供应商"),
    })
    @RequestMapping(value = "/getAllocationArrivalPlan")
    public PageResult getAllocationArrivalPlan(@RequestParam(defaultValue = "") String plant,
                                             @RequestParam(defaultValue = "") String material,
                                             @RequestParam(defaultValue = "") String supplier,
                                             @RequestParam Date startDate,
                                             @RequestParam Date endDate) {
        List list = getAllocationArrivalPlanData(plant, material, supplier, startDate, endDate);
        return ResultUtil.successPage(list, list.size());
    }

    private  List<Map<String, Object>> getAllocationArrivalPlanData(String plant, String material, String supplier, Date startDate, Date endDate) {
        List<Map> mapList = supplierArrivalPlanService.getAllocationArrivalPlan(startDate, endDate,  plant, material, supplier);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Integer> materialSupplerMap = new HashMap<>();
        for(Map map : mapList) {
            String material_ = (String) map.get("material");
            String supplier_ = (String) map.get("supplier");
            String plant_ = (String) map.get("plant");
            Double allocationQty = (Double) map.get("allocationQty");
            Double arrivalQty = (Double)  map.get("arrivalQty");
            String key = material_+supplier_;
            Date fabDate = (Date) map.get("fabDate");
            String materialName = (String) map.get("materialName");
            String materialGroup = (String) map.get("materialGroup");
            String supplierName = (String) map.get("supplierName");
            Integer k = materialSupplerMap.get(key);
            Map<String, Object> subMap = null;
            if(k!=null) {
                subMap = list.get(k);
            }
            if(subMap == null) {
                subMap = new HashMap<>();
                subMap.put("material", material_);
                subMap.put("supplier", supplier_);
                subMap.put("materialName", materialName);
                subMap.put("materialGroup", materialGroup);
                subMap.put("supplierName", supplierName);
                if(StringUtils.containsIgnoreCase(plant_, "LCM")) {
                    subMap.put("plant", "LCM");
                } else {
                    subMap.put("plant", plant_);
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
            qtyMap.put("arrivalQty", arrivalQty);
            fabDateMap.put(plant_, qtyMap);
        }
        return list;
    }

    @ApiOperation("获取开始结束时间的日历")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fromDate", value = "料号", required = true),
            @ApiImplicitParam(name = "toDate", value = "供应商ID", required = true),
    })
    @RequestMapping(value = "/getCalendar")
    public Result getCalendar(@RequestParam Date fromDate, @RequestParam Date toDate) {
        List<java.util.Date> list = DateUtil.getCalendar(fromDate, toDate);
        List<String> days = DateUtil.format_(list);
        String[] weeks = DateUtil.getWeekDay_(list);
        List<String> months = DateUtil.getMonthBetween(list.get(0), list.get(list.size()-1));
        Map<String, Object> map = new HashMap<>();
        map.put("days", days);
        map.put("weeks", weeks);
        map.put("months", months);
        return ResultUtil.success(map);
    }

    @ApiOperation("提交材料的到货计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "plant", value = "厂别", required = true),
            @ApiImplicitParam(name = "material", value = "料号", required = true),
            @ApiImplicitParam(name = "supplier", value = "供应商ID", required = true),
            @ApiImplicitParam(name = "fabDate", value = "日期", required = true),
            @ApiImplicitParam(name = "arrivalQty", value = "计划到货数量", required = true)
    })
    @PostMapping("/submitArrival")
    public Result submitArrival(String plant, String material, String supplier, Date fabDate, double arrivalQty) {
        supplierArrivalPlanService.saveSupplierArrivalPlan(plant, material, supplier, fabDate, arrivalQty);
        return ResultUtil.success();
    }

    @ApiOperation("保存提交材料的到货计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jsons", value = "提交材料的到货计划", required = true),
            @ApiImplicitParam(name = "plant", value = "厂别", required = true),
            @ApiImplicitParam(name = "material", value = "料号", required = true),
            @ApiImplicitParam(name = "fabDate", value = "日期", required = true)
    })
    @PostMapping("/bachSubmitArrival")
    public Result bachSubmitArrival( String plant,  String material, Date fabDate, String jsons) throws IOException, ParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map> mapList = objectMapper.readValue(jsons, new TypeReference<List<Map>>() {});
        if(mapList != null && mapList.size()>0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for(Map map : mapList) {
                String supplier = (String) map.get("supplier");
                double arrivalQty = Double.parseDouble(map.get("arrivalQty").toString() );
                supplierArrivalPlanService.saveSupplierArrivalPlan(plant, material, supplier, fabDate, arrivalQty);
            }
        }
        return ResultUtil.success();
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
                if(i<6) {
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

            List<SupplierArrivalPlan> supplierArrivalPlanList_1 = new ArrayList<>();
            List<SupplierArrivalPlan> supplierArrivalPlanList_2 = new ArrayList<>();
            String[] titles = new String[6];
            for(int i=2; i<sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                for(int j=0; j<row.getPhysicalNumberOfCells(); j++) {
                    Cell cell = row.getCell(j);
                    if(j<6) {
                        if(cell != null && StringUtils.isNotEmpty(cell.getStringCellValue())) {
                            titles[j] = cell.getStringCellValue();
                        }
                        continue;
                    }
                    if(StringUtils.containsIgnoreCase(titles[5], "要望")) continue;
                    Object object = ExcelUtil.getValue(cell);
                    Double qty = null;
                    if(object != null) {
                        if(object instanceof String) {
                            if(StringUtils.isNotEmpty( (String)object)) {
                                qty = Double.parseDouble((String) object);
                            }
                        } else if(object instanceof BigDecimal) {
                            qty = ((BigDecimal)object).doubleValue();
                        } else if(object instanceof Number) {
                            qty = ((Number) object).doubleValue();
                        }
                    }

                    String supplier = titles[0];
                    String material = titles[3];
                    Date fabDate = dateList.get(j);
                    String plant = null;
                    if(StringUtils.containsIgnoreCase(titles[5], "LCM1")) {
                        plant = "LCM1";
                    } else if(StringUtils.containsIgnoreCase(titles[5], "LCM2")) {
                        plant = "LCM2";
                    } else if(StringUtils.containsIgnoreCase(titles[5], "CELL")) {
                        plant = "CELL";
                    } else if(StringUtils.containsIgnoreCase(titles[5], "ARY")) {
                        plant = "ARY";
                    }
                    SupplierArrivalPlan supplierArrivalPlan = new SupplierArrivalPlan();
                    supplierArrivalPlan.setPlant(plant);
                    supplierArrivalPlan.setSupplier(supplier);
                    supplierArrivalPlan.setMaterial(material);
                    supplierArrivalPlan.setDate(fabDate);
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
            supplierArrivalPlanList_1.size();
            supplierArrivalPlanList_2.size();
            supplierArrivalPlanService.saveAll(supplierArrivalPlanList_1);
            supplierArrivalPlanService.deleteAll(supplierArrivalPlanList_2);
        } catch (IOException e) {
            return ResultUtil.error("上传失败：" + e.getMessage());
        } catch (DecryptException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ResultUtil.success();
    }

    @ApiOperation("供应商到货计划Excel上传模板下载")
    @GetMapping("/exportArrivalPlanDemo")
    public void exportArrivalPlanDemo() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        String[] titleItems = new String[] {"厂别", "料号", "供应商ID"};
        List<java.util.Date> dateList = DateUtil.getMonthEveryDays(new java.util.Date());
        Row row0 = sheet.createRow(0);
        int i=0;
        for(; i<titleItems.length; i++) {
            Cell cell = row0.createCell(i);
            cell.setCellValue(titleItems[i]);
        }
        int j=0;
        for(; j<dateList.size(); j++) {
            Cell cell = row0.createCell(i+j);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            cell.setCellValue(sdf.format(dateList.get(j)));
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

    @ApiOperation("供应商到货计划数据导出")
    @GetMapping("/exportArrivalPlan")
    public void exportArrivalPlan(Date fromDate, Date toDate, String plant) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        List<java.util.Date> dateList = DateUtil.getCalendar(fromDate, toDate);
        String[] weeks = DateUtil.getWeekDay_(dateList);
        String[] titleItems = new String[] {"供应商ID", "供应商", "厂别","料号","品名"};
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


        List<Map<String, Object>> list = getAllocationArrivalPlanData(plant, "", "", fromDate, toDate);
        for(int i=0; i<list.size(); i++) {
            intRow++;
            intCel=0;
            Map map = list.get(i);
            Row row_1 = sheet.createRow(intRow);
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
            if(StringUtils.equals(plant, "LCM")) {
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
            } else if(StringUtils.equals(plant, "CELL")) {
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
            } else if(StringUtils.equals(plant, "ARY")) {
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

    @ApiOperation("获取材料某日的到料计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "plant", value = "厂别", required = true),
            @ApiImplicitParam(name = "material", value = "料号", required = true),
            @ApiImplicitParam(name = "fabDate", value = "日期", required = true)
    })
    @RequestMapping("/getMaterialArrivalPlan")
    public PageResult getMaterialArrivalPlan(String plant, String material, Date fabDate) {
        List<SupplierArrivalPlan> supplierArrivalPlanList = supplierArrivalPlanService.getSupplierArrivalPlan(plant, material, fabDate);
        List<Supplier> supplierList = materialSupplierService.getMaterialSupplier(material);
        Set<String> supplierSet = new HashSet<>();
        List<Map> mapList = new ArrayList<>();
        for(SupplierArrivalPlan supplierArrivalPlan : supplierArrivalPlanList) {
            supplierSet.add(supplierArrivalPlan.getSupplier());
            Map<String, Object> map = new HashMap<>();
            map.put("supplier", supplierArrivalPlan.getSupplier());
            map.put("supplierName", supplierService.getSupplier(supplierArrivalPlan.getSupplier()).getSName());
            map.put("arrivalQty", supplierArrivalPlan.getArrivalQty());
            mapList.add(map);
        }
        for(Supplier supplier : supplierList) {
            if(supplierSet.contains(supplier.getId())) continue;
            Map<String, Object> map = new HashMap<>();
            map.put("supplier", supplier.getId());
            map.put("supplierName", supplierService.getSupplier(supplier.getId()).getSName());
            map.put("arrivalQty", 0);
            mapList.add(map);
        }
        return ResultUtil.successPage(mapList, mapList.size());
    }


}
