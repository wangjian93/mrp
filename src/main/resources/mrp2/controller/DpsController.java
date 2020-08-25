package com.ivo.mrp2.controller;

import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;
import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.DoubleUtil;
import com.ivo.common.utils.HttpServletUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.core.decryption.DecryptException;
import com.ivo.core.decryption.IVODecryptionUtils;
import com.ivo.mrp2.entity.DpsData;
import com.ivo.mrp2.entity.DpsVer;
import com.ivo.mrp2.service.DpsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DPS接口
 * @author wj
 * @version 1.0
 */
@Api(tags = "DPS接口")
@RestController
@RequestMapping("/mrp/dps")
public class DpsController {

    private DpsService dpsService;

    @Autowired
    public DpsController(DpsService dpsService) {
        this.dpsService = dpsService;
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
     * 分页查询DPS版本信息
     * @param page 页数
     * @param limit 分页大小
     * @return Result
     */
    @ApiOperation("分页查询DPS版本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50")
    })
    @GetMapping("/getPageDpsVer")
    public Result getDpsVer(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int limit) {
        Page p = dpsService.getDpsVer(page-1, limit);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    /**
     * 获取DPS的日期区间日历
     * @param ver dps版本
     * @return Result
     */
    @ApiOperation("获取DPS的日期区间日历")
    @ApiImplicitParam(name = "ver", value = "DPS版本", required = true)
    @GetMapping("/getDpsCalendar")
    public Result getDpsCalendar(@RequestParam String ver) {
        List list = dpsService.getDpsCalendarList(ver);
        List<String> days = DateUtil.format_(list);
        String[] weeks = DateUtil.getWeekDay_(list);
        Map<String, Object> map = new HashMap<>();
        map.put("days", days);
        map.put("weeks", weeks);
        return ResultUtil.success(map);
    }

    /**
     * 获取DPS某版本的数据
     * @param ver 版本
     * @return Result
     */
    @ApiOperation("获取DPS某版本的数据")
    @ApiImplicitParam(name = "ver", value = "DPS版本", required = true)
    @GetMapping("/getDps")
    public PageResult getDps(@RequestParam String ver) {
        DpsVer dpsVer = dpsService.getDpsVer(ver);
        List<DpsData> dpsDataList = dpsService.getDpsData(ver);
        Map<String, Map> productMap = new HashMap<>();
        for(DpsData dpsData : dpsDataList) {
            Map map = productMap.get(dpsData.getProduct());
            if(map==null) {
                map = new HashMap();
                map.put("product", dpsData.getProduct());
                map.put("model", dpsData.getModel());
                map.put("fab", dpsVer.getFab());
                map.put("totalQty", 0d);
                productMap.put(dpsData.getProduct(), map);
            }
            Map subMap = new HashMap();
            subMap.put("qty", dpsData.getQty());
            subMap.put("sum", dpsData.getQty());
            subMap.put("small", 0d);
            map.put(dpsData.getFabDate().toString(), subMap);
            map.put("totalQty", DoubleUtil.sum((double)map.get("totalQty"), (double)subMap.get("sum")));
        }

        //小版本
        if(StringUtils.isNotEmpty(dpsVer.getSmallVer())) {
            List<DpsData> smallDpsDataList = dpsService.getDpsData(dpsVer.getSmallVer());
            for(DpsData dpsData : smallDpsDataList) {
                Map map = productMap.get(dpsData.getProduct());
                if(map==null) {
                    map = new HashMap();
                    map.put("product", dpsData.getProduct());
                    map.put("model", dpsData.getModel());
                    map.put("fab", dpsVer.getFab());
                    map.put("totalQty", 0d);
                    productMap.put(dpsData.getProduct(), map);
                }
                Map subMap = (Map) map.get(dpsData.getFabDate().toString());
                if(subMap == null) {
                    subMap = new HashMap();
                    subMap.put("qty", 0d);
                    subMap.put("sum", 0d);
                    subMap.put("small", 0d);
                }
                subMap.put("small", dpsData.getQty());
                subMap.put("sum", DoubleUtil.sum((double)subMap.get("qty"), dpsData.getQty()));
                map.put(dpsData.getFabDate().toString(), subMap);
                map.put("totalQty", DoubleUtil.sum((double)map.get("totalQty"), (double)subMap.get("sum")));
            }
        }
        return ResultUtil.successPage(productMap.values(), productMap.values().size());
    }

    /**
     * 同步DPS数据
     * @return Result
     */
    @ApiOperation("同步DPS数据")
    @GetMapping("/syncDps")
    public Result syncDps() {
        dpsService.sync();
        return ResultUtil.success();
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
            dpsService.importDps(inputStream, file.getOriginalFilename());
        } catch (IOException e) {
            return ResultUtil.error("上传失败：" + e.getMessage());
        } catch (DecryptException e) {
            e.printStackTrace();
        }
        return ResultUtil.success();
    }

    /**
     * 获取DSP的版本信息
     * @param ver DPS版本
     * @return Result
     */
    @ApiOperation("获取DSP的版本信息")
    @ApiImplicitParam(name="ver", value = "DPS版本", required = true)
    @GetMapping("/getDpsVer")
    public Result getDpsVer(String ver) {
        return ResultUtil.success(dpsService.getDpsVer(ver));
    }

    /**
     * 调整DPS数量
     * @param ver 版本
     * @param product 机种
     * @param fabDate 日期
     * @param resizeQty 调整数量
     * @return Result
     */
    @ApiOperation("调整DPS数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name="ver", value = "DPS版本", required = true),
            @ApiImplicitParam(name="product", value = "机种", required = true),
            @ApiImplicitParam(name="fabDate", value = "日期", required = true),
            @ApiImplicitParam(name="resizeQty", value = "调整数量", required = true)
    })
    @PostMapping("/resizeQty")
    public Result resizeQty(String ver, String product, Date fabDate, double resizeQty) {
        dpsService.resizeQty(ver, product, fabDate, resizeQty);
        DpsVer dpsVer = dpsService.getDpsVer(ver);
        DpsData dpsDate = dpsService.getDpsDate(ver, product, fabDate);
        DpsData rDpsData = dpsService.getDpsDate(dpsVer.getSmallVer(), product, fabDate);
        Map map = new HashMap();
        map.put("product", product);
        map.put("fabDate", fabDate);
        map.put("qty", 0d);
        map.put("small", 0d);
        map.put("sum", 0d);
        if(dpsDate!=null)  map.put("qty", dpsDate.getQty());
        if(rDpsData!=null)  map.put("small", rDpsData.getQty());
        map.put("sum", DoubleUtil.sum( (Double)map.get("qty"), (Double)map.get("small")));
        return ResultUtil.success(map);
    }

    /**
     * 取消DPS的调整
     * @param ver 版本
     * @param product 机种
     * @param fabDate 日期
     * @return Result
     */
    @ApiOperation("取消DPS的调整")
    @ApiImplicitParams({
            @ApiImplicitParam(name="ver", value = "DPS版本", required = true),
            @ApiImplicitParam(name="product", value = "机种", required = true),
            @ApiImplicitParam(name="fabDate", value = "日期", required = true),
    })
    @PostMapping("/cancelResize")
    public Result cancelResize(String ver, String product, Date fabDate) {
        dpsService.cancelResize(ver, product, fabDate);
        DpsData dpsDate = dpsService.getDpsDate(ver, product, fabDate);
        Map map = new HashMap();
        map.put("product", product);
        map.put("fabDate", fabDate);
        map.put("qty", 0d);
        map.put("small", 0d);
        map.put("sum", 0d);
        if(dpsDate!=null)  map.put("qty", dpsDate.getQty());
        map.put("sum", DoubleUtil.sum( (Double)map.get("qty"), (Double)map.get("small")));
        return ResultUtil.success(map);
    }

}
