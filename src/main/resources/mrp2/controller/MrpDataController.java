package com.ivo.mrp2.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;
import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.DoubleUtil;
import com.ivo.common.utils.HttpServletUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.entity.MrpData;
import com.ivo.mrp2.entity.MrpDataMaster;
import com.ivo.mrp2.entity.MrpVer;
import com.ivo.mrp2.repository.MrpDataRepository;
import com.ivo.mrp2.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * MRP数据接口
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/mrpData")
@Api(tags = "MRP数据接口")
public class MrpDataController {

    private MrpDataMasterService mrpDataMasterService;

    private MrpDataService mrpDataService;

    private MrpService2 mrpService;

    private MrpVerService mrpVerService;

    private ProductCustomerService productCustomerService;

    private MaterialGroupService materialGroupService;

    private MrpDataRepository mrpDataRepository;

    private MaterialService materialService;

    @Autowired
    public MrpDataController(MrpDataMasterService mrpDataMasterService, MrpDataService mrpDataService,
                             MrpService2 mrpService,
                             MrpVerService mrpVerService,
                             ProductCustomerService productCustomerService,
                             MaterialGroupService materialGroupService,
                             MrpDataRepository mrpDataRepository,
                             MaterialService materialService) {
        this.mrpDataMasterService = mrpDataMasterService;
        this.mrpDataService = mrpDataService;
        this.mrpService = mrpService;
        this.mrpVerService = mrpVerService;
        this.productCustomerService = productCustomerService;
        this.materialGroupService = materialGroupService;
        this.mrpDataRepository = mrpDataRepository;
        this.materialService = materialService;
    }

    @ApiOperation("分页查询MRP数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "mrpVer", value = "MRP版本", required = true),
            @ApiImplicitParam(name = "product", value = "机种"),
            @ApiImplicitParam(name = "materialGroup", value = "物料组"),
            @ApiImplicitParam(name = "material", value = "料号")
    })
    @GetMapping("/getPageMrpData")
    public PageResult getPageMrpData(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "50") int limit,
                                     String mrpVer,
                                     @RequestParam(defaultValue = "") String product,
                                     @RequestParam(defaultValue = "") String materialGroup,
                                     @RequestParam(defaultValue = "") String material,
                                     @RequestParam(defaultValue = "") String supplier) {
        Page<MrpDataMaster> p = mrpDataMasterService.getPageMrpData(page-1, limit, mrpVer, product, materialGroup, material, supplier);

        List<Map<String, Object>> list = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> materialList = new ArrayList<>();
        MrpVer m = mrpVerService.getMrpVer(mrpVer);
        List<String> months = DateUtil.getMonthBetween(m.getStartDate(), m.getEndDate());
        int i=0;
        Map<String, Integer> tagMap = new HashMap<>();
        for(MrpDataMaster mrpDataMaster : p.getContent()) {
            materialList.add(mrpDataMaster.getMaterial());
            Map<String, Object> map = objectMapper.convertValue(mrpDataMaster, new TypeReference<Map>(){});
            //月份汇总
            for(String month : months) {
                map.put(month, new HashMap<String, Double>());
            }
            list.add(map);
            tagMap.put(mrpDataMaster.getMaterial(), i++);
        }
        List<MrpData> mrpDataList = mrpDataService.getMrpData(mrpVer, materialList);
        for(MrpData mrpData : mrpDataList) {
            Map map = list.get(tagMap.get(mrpData.getMaterial()));
            if(map == null) continue;
            map.put(mrpData.getFabDate().toString(), mrpData);

            //处理月份汇总
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月
            String month = sdf.format(mrpData.getFabDate());
            Map<String, Double> monthMap = (Map<String, Double>) map.get(month);
            monthMap.put("demandQty", DoubleUtil.sum(monthMap.get("demandQty"), mrpData.getDemandQty()));
            monthMap.put("lossQty", DoubleUtil.sum(monthMap.get("lossQty"), mrpData.getLossQty()));
            monthMap.put("arrivalQty", DoubleUtil.sum(monthMap.get("arrivalQty"), mrpData.getArrivalQty()));
            monthMap.put("shortQty", DoubleUtil.sum(monthMap.get("shortQty"), mrpData.getShortQty()));
            monthMap.put("allocationQty", DoubleUtil.sum(monthMap.get("allocationQty"), mrpData.getAllocationQty()));
            monthMap.put("balanceQty",  mrpData.getBalanceQty());
        }
        return ResultUtil.successPage(list, p.getTotalElements());
    }

    @ApiOperation("获取材料需求的详细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mrpVer", value = "MRP版本", required = true),
            @ApiImplicitParam(name = "material", value = "料号", required = true),
            @ApiImplicitParam(name = "fabDate", value = "日期", required = true)
    })
    @GetMapping("/getMaterialDemandDetail")
    public PageResult getMaterialDemandDetail(String mrpVer, String material, @DateTimeFormat(pattern = "yyyy-MM-dd") Date fabDate) {
        List list = mrpDataService.getMaterialDemandDetail(mrpVer, material, new java.sql.Date(fabDate.getTime()));
        return ResultUtil.successPage(list, list.size());
    }

    @ApiOperation("更新MRP的某单个材料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mrpVer", value = "MRP版本", required = true),
            @ApiImplicitParam(name = "material", value = "料号", required = true),
    })
    @GetMapping("/updateMrpMaterial")
    public Result updateMrpMaterial(String mrpVer, String material) {
        MrpVer m = mrpVerService.getMrpVer(mrpVer);
        MrpDataMaster mrpDataMaster = mrpDataMasterService.getMrpMaterial(mrpVer, material);
        if(m == null || mrpDataMaster == null) {
            return  ResultUtil.error("料号在MRP中不存在");
        }
        mrpService.updateMrp(m, mrpDataMaster);
        List<MrpData> mrpDataList = mrpDataService.getMrpData(mrpVer, material);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(mrpDataMaster, new TypeReference<Map>(){});
        List<String> months = DateUtil.getMonthBetween(m.getStartDate(), m.getEndDate());
        //月份汇总
        for(String month : months) {
            map.put(month, new HashMap<String, Double>());
        }
        for(MrpData mrpData : mrpDataList) {
            map.put(mrpData.getFabDate().toString(), mrpData);

            //处理月份汇总
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月
            String month = sdf.format(mrpData.getFabDate());
            Map<String, Double> monthMap = (Map<String, Double>) map.get(month);
            monthMap.put("demandQty", DoubleUtil.sum(monthMap.get("demandQty"), mrpData.getDemandQty()));
            monthMap.put("lossQty", DoubleUtil.sum(monthMap.get("lossQty"), mrpData.getLossQty()));
            monthMap.put("arrivalQty", DoubleUtil.sum(monthMap.get("arrivalQty"), mrpData.getArrivalQty()));
            monthMap.put("shortQty", DoubleUtil.sum(monthMap.get("shortQty"), mrpData.getShortQty()));
            monthMap.put("allocationQty", DoubleUtil.sum(monthMap.get("allocationQty"), mrpData.getAllocationQty()));
            monthMap.put("balanceQty",  mrpData.getBalanceQty());
        }
        return  ResultUtil.success(map);
    }

    @ApiOperation("修改结余量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mrpVer", value = "MRP版本", required = true),
            @ApiImplicitParam(name = "material", value = "料号", required = true),
            @ApiImplicitParam(name = "fabDate", value = "日期", required = true),
            @ApiImplicitParam(name = "balanceQty", value = "结余量", required = true),
            @ApiImplicitParam(name = "memo", value = "备注", required = true),
    })
    @PostMapping("/editBalance")
    public Result editBalance(String mrpVer, String material, java.sql.Date fabDate, double balanceQty, String  memo) {
        mrpDataService.editBalance(mrpVer, material, fabDate, balanceQty, memo);
        return  ResultUtil.success();
    }


    @ApiOperation("获取缺料报表数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = true),
            @ApiImplicitParam(name = "endDate", value = "结束日期", required = true),
            @ApiImplicitParam(name = "plant", value = "厂别", required = true),
            @ApiImplicitParam(name = "material", value = "料号"),
            @ApiImplicitParam(name = "product", value = "机种"),
            @ApiImplicitParam(name = "materialGroup", value = "物料组")
    })
    @GetMapping("/getShortData")
    public PageResult getShortData(
            @RequestParam java.sql.Date startDate,
            @RequestParam java.sql.Date endDate,
            @RequestParam String plant,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "") String material,
            @RequestParam(defaultValue = "") String product,
            @RequestParam(defaultValue = "") String materialGroup) {
        List<String> mrpVers = new ArrayList<>();
        if(StringUtils.equals(plant, "LCM")) {
            mrpVers.add("20200729100934");
            mrpVers.add("20200820112726");
        } else if(StringUtils.equals(plant, "LCM1")) {
            mrpVers.add("20200729100934");
        } else if(StringUtils.equals(plant, "LCM2")) {
            mrpVers.add("20200820112726");
        }
        else if(StringUtils.equals(plant, "CELL")) {
            mrpVers.add("20200819165556");
        } else if(StringUtils.equals(plant, "ARY")) {
            mrpVers.add("20200821112641");
        }

        Page p = mrpDataMasterService.getPageMrpData(page-1, limit, mrpVers, product, materialGroup, material);
        List<String> materialList = p.getContent();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Integer> tagMap = new HashMap<>();
        for(String ver : mrpVers) {
            MrpVer mrpVer = mrpVerService.getMrpVer(ver);
            List<MrpData> mrpDataList = mrpDataService.getMrpData(ver, materialList, startDate, endDate);
            for(MrpData mrpData : mrpDataList) {
                Integer tag = tagMap.get(mrpData.getMaterial());
                Map<String, Object> map;
                if(tag != null) {
                    map = list.get(tag);
                } else {
                    map = new HashMap<>();
                    map.put("material", mrpData.getMaterial());
                    map.put("plant", plant);
                    list.add(map);
                    tagMap.put(mrpData.getMaterial(), list.size()-1);
                }

                String fabDate = mrpData.getFabDate().toString();
                Map subMap = (Map) map.get(fabDate);
                if(subMap == null) {
                    subMap = new HashMap();
                    map.put(fabDate, subMap);
                }
                subMap.put(mrpVer.getPlant(), mrpData.getShortQty());
            }
        }
        for(Map<String, Object> map : list) {
            String material_ = (String) map.get("material");
            MrpDataMaster mrpDataMaster = mrpDataMasterService.getMrpMaterial(mrpVers.get(0), material_);
            if(mrpDataMaster == null) {
                mrpDataMaster = mrpDataMasterService.getMrpMaterial(mrpVers.get(1), material_);
            }
            if(mrpDataMaster == null) continue;
            map.put("materialName", mrpDataMaster.getMaterialName());
            map.put("materialGroup", mrpDataMaster.getMaterialGroup());
            map.put("product", mrpDataMaster.getProducts());
            String products =  mrpDataMaster.getProducts();
            String[] products_ = products.split(",");
            List<String> productList = Arrays.asList(products_);
            List<String> customerList = productCustomerService.getCustomer(productList);
            String customer = "";
            if(customerList != null & customerList.size() >0) {
                for(String c : customerList) {
                    if(!customer.equals("")) {
                        customer += "," + c;
                    } else {
                        customer += c;
                    }
                }
            }
            map.put("customer", customer);
        }
        return ResultUtil.successPage(list, p.getTotalElements());
    }

    @ApiOperation("更新MRP")
    @ApiImplicitParam(name = "mrpVer", value = "MRP版本")
    @GetMapping("/updateMrp")
    public Result updateMrp(String mrpVer) {
        MrpVer m = mrpVerService.getMrpVer(mrpVer);
        if(m == null) {
            return ResultUtil.error("选择的MRP版本不存在");
        }
        mrpService.updateMrp(m);
        return ResultUtil.success();
    }




    @GetMapping("/getPageMrpDataPrPo")
    public PageResult getPageMrpDataPrPo(String mrpVer,
                                     @RequestParam(required = false) String materialGroup) throws ParseException {
        MrpVer m = mrpVerService.getMrpVer(mrpVer);
        List<Map> maps = mrpDataRepository.getSumMaterialGroup(mrpVer, materialGroup + "%");
        Map<String, Integer> tabMap = new HashMap<>();
        List<Map> mapList = new ArrayList<>();
        for(Map map : maps) {
            String materialGroup_ = (String) map.get("materialGroup");
            Map sunMap;
            if(tabMap.get(materialGroup_) == null) {
                sunMap = new HashMap();
                sunMap.put("mrpVer", mrpVer);
                sunMap.put("plant", m.getPlant());
                sunMap.put("materialGroup", materialGroup_);
                sunMap.put("materialGroupName", map.get("materialGroupName"));
                sunMap.put("goodInventory", map.get("goodInventory"));
                sunMap.put("dullInventory", map.get("dullInventory"));
                tabMap.put(materialGroup_, mapList.size());
                mapList.add(sunMap);
            } else {
                sunMap =  mapList.get(tabMap.get(materialGroup_));
            }
            String month = (String) map.get("month");
            //open PR PO
            Map prMap = mrpDataRepository.getOpenPr(month+"%", materialGroup_);
            Map poMap = mrpDataRepository.getOpenPo(month+"%", materialGroup_);

            Map monthMap = new HashMap();
            monthMap.put("demandQty", map.get("demandQty"));
            monthMap.put("qty_pr", prMap.get("qty"));
            monthMap.put("orderQty_pr", prMap.get("orderQty"));
            monthMap.put("rQty_pr", prMap.get("rQty"));

            monthMap.put("qty_po", poMap.get("qty"));
            monthMap.put("orderQty_po", poMap.get("orderQty"));
            monthMap.put("rQty_po", poMap.get("rQty"));
            sunMap.put(month, monthMap);
        }
        //求结余
        List<String> months = DateUtil.getMonthBetween(m.getStartDate(), m.getEndDate());
        for(Map sunMap : mapList) {
            double goodInventory = 0;
            if(sunMap.get("goodInventory") != null) {
                goodInventory = ((BigDecimal) sunMap.get("goodInventory")).doubleValue();
            }
            for(int i=0; i<months.size(); i++) {
                String month = months.get(i);
                Map monthMap = (Map)sunMap.get(month);
                if(monthMap == null) {
                    monthMap = new HashMap();
                    sunMap.put(month, monthMap);
                }
                double demandQty = 0;
                double rQty_pr = 0;
                double rQty_po = 0;
                if(monthMap.get("demandQty") != null) {
                    demandQty = ((BigDecimal) monthMap.get("demandQty")).doubleValue();
                }
                if(monthMap.get("rQty_pr") != null) {
                    rQty_pr = ((BigDecimal) monthMap.get("rQty_pr")).doubleValue();
                }
                if(monthMap.get("rQty_po") != null) {
                    rQty_po = ((BigDecimal) monthMap.get("rQty_po")).doubleValue();
                }
                if(i == 0) {
                    double balanceQty = goodInventory + rQty_pr + rQty_po - demandQty;
                    monthMap.put("balanceQty", balanceQty);
                } else {
                    Map monthMap_last = (Map) sunMap.get(months.get(i-1));
                    double balanceQty_last = (double) monthMap_last.get("balanceQty");
                    double balanceQty = balanceQty_last + rQty_pr + rQty_po - demandQty;
                    monthMap.put("balanceQty", balanceQty);
                }
            }
        }
        return ResultUtil.successPage(mapList, mapList.size());
    }

    @ApiOperation("MRP数据导出")
    @GetMapping("/exportMrpData")
    public void exportArrivalPlan(String mrpVer, String materialGroup) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        MrpVer m = mrpVerService.getMrpVer(mrpVer);
        List<Date> dateList = DateUtil.getCalendar(m.getStartDate(), m.getEndDate());
        String[] weeks = DateUtil.getWeekDay_(dateList);
        String[] titleItems = new String[] {"料号", "物料描述", "机种","厂别","物料组","供应商","损耗率","良品库存","呆滞库存"};
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
        for(Date date : dateList) {
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
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 7, 7));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 8, 8));
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 9, 9));

        Page<MrpDataMaster> p = mrpDataMasterService.getPageMrpData(0, 5000, mrpVer, "", materialGroup, "", "");
        List<Map<String, Object>> list = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> materialList = new ArrayList<>();
        List<String> months = DateUtil.getMonthBetween(m.getStartDate(), m.getEndDate());
        int i=0;
        Map<String, Integer> tagMap = new HashMap<>();
        for(MrpDataMaster mrpDataMaster : p.getContent()) {
            materialList.add(mrpDataMaster.getMaterial());
            Map<String, Object> map = objectMapper.convertValue(mrpDataMaster, new TypeReference<Map>(){});
            //月份汇总
            for(String month : months) {
                map.put(month, new HashMap<String, Double>());
            }
            list.add(map);
            tagMap.put(mrpDataMaster.getMaterial(), i++);
        }
        List<MrpData> mrpDataList = mrpDataService.getMrpData(mrpVer, materialList);
        for(MrpData mrpData : mrpDataList) {
            Map map = list.get(tagMap.get(mrpData.getMaterial()));
            if(map == null) continue;
            map.put(mrpData.getFabDate().toString(), mrpData);

            //处理月份汇总
            SimpleDateFormat sdf_m = new SimpleDateFormat("yyyy-MM");//格式化为年月
            String month = sdf_m.format(mrpData.getFabDate());
            Map<String, Double> monthMap = (Map<String, Double>) map.get(month);
            monthMap.put("demandQty", DoubleUtil.sum(monthMap.get("demandQty"), mrpData.getDemandQty()));
            monthMap.put("lossQty", DoubleUtil.sum(monthMap.get("lossQty"), mrpData.getLossQty()));
            monthMap.put("arrivalQty", DoubleUtil.sum(monthMap.get("arrivalQty"), mrpData.getArrivalQty()));
            monthMap.put("shortQty", DoubleUtil.sum(monthMap.get("shortQty"), mrpData.getShortQty()));
            monthMap.put("allocationQty", DoubleUtil.sum(monthMap.get("allocationQty"), mrpData.getAllocationQty()));
            monthMap.put("balanceQty",  mrpData.getBalanceQty());
        }

        intRow++;
        intCel=0;
        CellStyle cs = workbook.createCellStyle();
        cs.setWrapText(true);  //换行
        for(Map<String, Object> map : list) {
            Row row_1 = sheet.createRow(intRow++);
            row_1.createCell(intCel++).setCellValue( (String)map.get("material") );
            row_1.createCell(intCel++).setCellValue( (String)map.get("materialName"));
            row_1.createCell(intCel++).setCellValue( (String)map.get("products") );
            row_1.createCell(intCel++).setCellValue( (String)map.get("plant") );
            row_1.createCell(intCel++).setCellValue( (String)map.get("materialGroup") );
            row_1.createCell(intCel++).setCellValue( (String)map.get("supplier") );
            row_1.createCell(intCel++).setCellValue( (double)map.get("lossRate") );
            row_1.createCell(intCel++).setCellValue( (double)map.get("goodInventory") );
            row_1.createCell(intCel++).setCellValue( (double)map.get("dullInventory") );
            row_1.createCell(intCel++).setCellValue( "" );
            intCel=0;
        }


        HttpServletResponse response = HttpServletUtil.getResponse();
        response.setContentType("application/vnd.ms-excel;chartset=utf-8");
        String fileName = "MRP数据";
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
}
