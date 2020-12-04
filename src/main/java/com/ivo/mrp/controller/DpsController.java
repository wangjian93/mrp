package com.ivo.mrp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.result.Result;
import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.DoubleUtil;
import com.ivo.common.utils.HttpServletUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.core.decryption.DecryptException;
import com.ivo.core.decryption.IVODecryptionUtils;
import com.ivo.mrp.entity.DpsVer;
import com.ivo.mrp.entity.direct.ary.DpsAry;
import com.ivo.mrp.entity.direct.cell.DpsCell;
import com.ivo.mrp.entity.direct.cell.DpsCellOutputName;
import com.ivo.mrp.entity.direct.lcm.DpsLcm;
import com.ivo.mrp.service.DpsOutputNameService;
import com.ivo.mrp.service.DpsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DPS数据接口
 * @author wj
 * @version 1.0
 */
@Api(tags = "DPS数据接口")
@RestController
@RequestMapping("/mrp/dps")
@Slf4j
public class DpsController {

    private DpsService dpsService;

    private DpsOutputNameService dpsOutputNameService;

    @Autowired
    public DpsController(DpsService dpsService, DpsOutputNameService dpsOutputNameService) {
        this.dpsService = dpsService;
        this.dpsOutputNameService = dpsOutputNameService;
    }

    @ApiOperation("查询DPS版本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "fab", value = "查询厂别"),
            @ApiImplicitParam(name = "type", value = "查询类型"),
            @ApiImplicitParam(name = "ver", value = "查询版本")
    })
    @GetMapping("/queryDpsVer")
    public Result queryDpsVer(@RequestParam(required = false, defaultValue = "1") int page,
                                @RequestParam(required = false, defaultValue = "50") int limit,
                                @RequestParam(required = false, defaultValue = "") String fab,
                                @RequestParam(required = false, defaultValue = "") String type,
                                @RequestParam(required = false, defaultValue = "") String ver) {
        Page p = dpsService.queryDpsVer(page-1, limit, fab, type, ver);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    @ApiOperation("获取DPS版本数据")
    @ApiImplicitParam(name = "ver", value = "DPS版本", required = true)
    @GetMapping("/getDpsData")
    public Result getDpsData(String ver) {
        List list = dpsService.getDpsDate(ver);
        return ResultUtil.success(list);
    }

    @ApiOperation("分页获取DPS的机种")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ver", value = "查询版本"),
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "product", value = "查询机种"),
    })
    @GetMapping("/getPageDpsProduct")
    public Result getPageDpsProduct(String ver,
                                    @RequestParam(required = false, defaultValue = "1") int page,
                                    @RequestParam(required = false, defaultValue = "50") int limit,
                                    @RequestParam(required = false, defaultValue = "") String product) {
        Page p = dpsService.getPageProduct(ver, page-1, limit, product);
        DpsVer dpsVer = dpsService.getDpsVer(ver);
        String type = dpsVer.getType();
        List<Map> mapList = new ArrayList<>();
        List<Map> pml = p.getContent();
        for(Map pm : pml) {
            String pro = (String) pm.get("product");
            String fab = (String) pm.get("fab");
            String project = (String) pm.get("project");
            String outputName = (String) pm.get("outputName");
            Map<String, Object> map = new HashMap<>();
            map.put("product", pro);
            map.put("fab", fab);
            map.put("project", project);
            map.put("outputName", outputName);
            List list;
            //月份汇总
            switch (type) {
                case DpsVer.Type_Ary:
                    list = dpsService.getDpsAry(ver, pro);
                    for(Object object : list) {
                        DpsAry dpsAry = (DpsAry) object;
                        double demandQty = dpsAry.getDemandQty();
                        String day = dpsAry.getFabDate().toString();
                        String month = day.substring(0,7);
                        map.put(day, dpsAry);
                        map.putIfAbsent(month, 0D);
                        map.put(month, DoubleUtil.rounded((double)map.get(month)+demandQty, 3));
                    }
                    break;
                case DpsVer.Type_Cell:
                    list = dpsService.getDpsCell(ver, pro);
                    for(Object object : list) {
                        DpsCell dpsCell = (DpsCell) object;
                        double demandQty = dpsCell.getDemandQty();
                        String day = dpsCell.getFabDate().toString();
                        String month = day.substring(0,7);
                        map.put(day, dpsCell);
                        map.putIfAbsent(month, 0D);
                        map.put(month, DoubleUtil.rounded((double)map.get(month)+demandQty, 3));
                    }
                    break;
                case DpsVer.Type_Lcm:
                    list = dpsService.getDpsLcm(ver, pro);
                    for(Object object : list) {
                        DpsLcm dpsLcm = (DpsLcm) object;
                        double demandQty = dpsLcm.getDemandQty();
                        String day = dpsLcm.getFabDate().toString();
                        String month = day.substring(0,7);
                        map.put(day, dpsLcm);
                        map.putIfAbsent(month, 0D);
                        map.put(month, DoubleUtil.rounded( (double)map.get(month)+demandQty, 3));
                    }
                    break;
                default:
                    p = null;
            }
            mapList.add(map);
        }
        if(p==null) {
            return ResultUtil.successPage(new ArrayList(),0);
        }
        return ResultUtil.successPage(mapList, p.getTotalElements());
    }

    @ApiOperation("获取DPS的日历")
    @ApiImplicitParam(name = "ver", value = "DPS版本", required = true)
    @GetMapping("/getDpsCalendar")
    public Result getDpsCalendar(String ver) {
        List list = dpsService.getDpsCalendar(ver);
        List<String> days = DateUtil.format_(list);
        String[] weeks = DateUtil.getWeekDay_(list);
        List<String> months = DateUtil.getMonthBetween((Date) list.get(0), (Date) list.get(list.size()-1));
        Map<String, Object> map = new HashMap<>();
        map.put("days", days);
        map.put("weeks", weeks);
        map.put("months", months);
        return ResultUtil.success(map);
    }

    /**
     * DPS导入模板
     */
    @RequestMapping("/exportDpsDemo")
    public void exportDpsDemo() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        Row row0 = sheet.createRow(0);
        Cell cell0 = row0.createCell(0);
        cell0.setCellValue("LCM1");

        String[] titleItems = new String[] {"Size", "机种"};
        List<java.util.Date> dateList = DateUtil.getMonthEveryDays(new java.util.Date());
        Row row1 = sheet.createRow(1);

        //设置单元格格式为日期类型
        CellStyle cellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        short dateFormat = createHelper.createDataFormat().getFormat("yyyy/MM/dd");
        cellStyle.setDataFormat(dateFormat);

        int i=0;
        for(; i<titleItems.length; i++) {
            Cell cell = row1.createCell(i);
            cell.setCellValue(titleItems[i]);
        }
        int j=0;
        for(; j<dateList.size(); j++) {
            Cell cell = row1.createCell(i+j);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            cell.setCellValue(sdf.format(dateList.get(j)));
        }

        HttpServletResponse response = HttpServletUtil.getResponse();
        response.setContentType("application/vnd.ms-excel;chartset=utf-8");
        String fileName = "DPS导入模板";
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

    /**
     * MC手动Excel上传DPS
     * @param file 文件
     * @return Result
     */
    @ApiOperation("MC手动Excel上传DPS")
    @ApiImplicitParam(name="file", value = "上传的excel", required = true)
    @PostMapping(value = "/importDps", headers = "content-type=multipart/form-data")
    public Result importDps(@RequestParam("file") MultipartFile file) {
        try {
            //IVO文件解密
            byte[] bytes = IVODecryptionUtils.decrypt(file.getInputStream());
            InputStream inputStream = new ByteArrayInputStream(bytes);
            dpsService.importLcmDps(inputStream, file.getOriginalFilename());
        } catch (IOException e) {
            return ResultUtil.error("上传失败：" + e.getMessage());
        } catch (DecryptException e) {
            e.printStackTrace();
        }
        return ResultUtil.success();
    }


    @ApiOperation("获取OutputName的数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ver", value = "DPS版本"),
            @ApiImplicitParam(name = "outputName", value = "outputName"),
    })
    @GetMapping("/getDpsOutputName")
    public Result getDpsOutputName(String ver, String outputName) {
        List<DpsCellOutputName> dpsCellOutputNameList = dpsOutputNameService.getDpsCellOutputName(ver, outputName);
        List<DpsCell> dpsCellList = dpsService.getDpsCellByOutputName(ver, outputName);
        String[] products = outputName.split(",");
        List<String> productList = new ArrayList<>();
        List<Map> mapList = new ArrayList<>();
        for(DpsCellOutputName dpsCellOutputName : dpsCellOutputNameList) {
            String product = "";
            Map map;
            if(productList.contains(product)) {
                map = mapList.get(productList.indexOf(product));
            } else {
                map = new HashMap();
                map.put("product", product);
                productList.add(product);
                mapList.add(map);
            }
            map.put(dpsCellOutputName.getFabDate().toString(), dpsCellOutputName.getDemandQty());
        }
        for(DpsCell dpsCell : dpsCellList) {
            String product =  dpsCell.getProduct();
            Map map;
            if(productList.contains(product)) {
                map = mapList.get(productList.indexOf(product));
            } else {
                map = new HashMap();
                map.put("product", product);

                productList.add(product);
                mapList.add(map);
            }
            map.put(dpsCell.getFabDate().toString(), dpsCell.getDemandQty());
        }
        for(String product : products) {
            if(productList.contains(product)) continue;
            Map map = new HashMap();
            map.put("product", product);

            productList.add(product);
            mapList.add(map);
        }
        return ResultUtil.successPage(mapList, mapList.size());
    }

    @ApiOperation("OutputName的拆分数据提交")
    @PostMapping("/submitDpsOutputName")
    public Result submitDpsOutputName(String ver, String outputName, String jsonData) {
        List<DpsCell> dpsCellList = dpsService.getDpsCellByOutputName(ver, outputName);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map> mapList;
        try {
            mapList = objectMapper.readValue(jsonData, new TypeReference<List<Map>>() {});
        } catch (IOException e) {
            log.error("数据解析错误", e);
            return ResultUtil.error("提交数据解析错误");
        }
        List<Date> dateList = dpsService.getDpsCalendar(ver);
        List<DpsCell> newDpsCellList = new ArrayList<>();
        for(Map map : mapList) {
            String product = (String) map.get("product");
            String project = (String) map.get("project");
            for(Date fabDate : dateList) {
                Object qtyObject = map.get(fabDate.toString());
                if(qtyObject == null) continue;

                Double qty;
                if(qtyObject instanceof Double) {
                    qty = (Double) qtyObject;
                } else if(qtyObject instanceof Integer) {
                    qty = (Double) qtyObject;
                } else {
                    String str = (String)qtyObject;
                    if(StringUtils.isEmpty(str)) {
                        qty = 0D;
                    } else {
                        qty = Double.parseDouble(str);
                    }
                }
                if(qty == null || qty == 0) continue;
                DpsCell dpsCell = new DpsCell();
                dpsCell.setVer(ver);
                dpsCell.setFab("CELL");
                dpsCell.setOutputName(outputName);
                dpsCell.setProduct(product);
                dpsCell.setProject(project);
                dpsCell.setFabDate(fabDate);
                dpsCell.setDemandQty(qty);

                newDpsCellList.add(dpsCell);
            }
        }

        dpsService.deleteDpsCell(dpsCellList);
        dpsService.saveDpsCell(newDpsCellList);
        return ResultUtil.success("提交成功");
    }
}
