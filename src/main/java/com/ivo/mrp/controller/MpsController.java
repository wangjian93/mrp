package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.DoubleUtil;
import com.ivo.common.utils.HttpServletUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.core.decryption.DecryptException;
import com.ivo.core.decryption.IVODecryptionUtils;
import com.ivo.mrp.entity.DpsVer;
import com.ivo.mrp.entity.MpsVer;
import com.ivo.mrp.entity.direct.ary.DpsAry;
import com.ivo.mrp.entity.direct.ary.MpsAry;
import com.ivo.mrp.entity.direct.cell.DpsCell;
import com.ivo.mrp.entity.direct.cell.MpsCell;
import com.ivo.mrp.entity.direct.lcm.DpsLcm;
import com.ivo.mrp.entity.direct.lcm.MpsLcm;
import com.ivo.mrp.service.MpsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MPS数据接口
 * @author wj
 * @version 1.0
 */
@Api(tags = "MPS数据接口")
@RestController
@RequestMapping("/mrp/mps")
public class MpsController {

    private MpsService mpsService;

    @Autowired
    public MpsController(MpsService mpsService) {
        this.mpsService = mpsService;
    }

    @ApiOperation("查询MPS版本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "fab", value = "查询厂别"),
            @ApiImplicitParam(name = "ver", value = "查询版本")
    })
    @GetMapping("/queryMpsVer")
    public Result queryMpsVer(@RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "50") int limit,
                              @RequestParam(required = false, defaultValue = "") String fab,
                              @RequestParam(required = false, defaultValue = "") String ver) {
        Page p = mpsService.queryMpsVer(page-1, limit, fab, ver);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    @ApiOperation("获取MPS版本数据")
    @ApiImplicitParam(name = "ver", value = "Mps版本", required = true)
    @GetMapping("/getMpsData")
    public Result getMpsData(String ver) {
        List list = mpsService.getMpsDate(ver);
        return ResultUtil.success(list);
    }

    @ApiOperation("获取MPS的日历")
    @ApiImplicitParam(name = "ver", value = "DPS版本", required = true)
    @GetMapping("/getMpsCalendar")
    public Result getMpsCalendar(String ver) {
        List<java.util.Date> list = mpsService.getMpsCalendar(ver);
        List<String> days = DateUtil.format_(list);
        Map<String, Object> map = new HashMap<>();
        map.put("days", days);
        return ResultUtil.success(map);
    }

    @ApiOperation("分页获取MPS的机种")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ver", value = "查询版本"),
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "product", value = "查询机种"),
    })
    @GetMapping("/getPageMpsProduct")
    public Result getPageMpsProduct(String ver,
                                    @RequestParam(required = false, defaultValue = "1") int page,
                                    @RequestParam(required = false, defaultValue = "50") int limit,
                                    @RequestParam(required = false, defaultValue = "") String product) {
        MpsVer mpsVer = mpsService.getMpsVer(ver);
        String type = mpsVer.getType();
        Map<String, Integer> map = new HashMap<>();
        List<Map> mapList = new ArrayList();
        switch (type) {
            case DpsVer.Type_Ary:
                List<MpsAry> list = mpsService.getMpsAry(ver);
                for (MpsAry mpsAry : list) {
                    String p = mpsAry.getProduct();
                    if(!StringUtils.containsIgnoreCase(p, product)) continue;
                    Integer i = map.get(p);
                    if (i == null) {
                        Map m = new HashMap();
                        m.put("product", p);
                        m.put("fab", mpsVer.getFab());
                        m.put(mpsAry.getFabDate().toString(), mpsAry.getDemandQty());
                        mapList.add(m);
                        map.put(p, mapList.size() - 1);
                    } else {
                        Map m = mapList.get(i);
                        m.put(mpsAry.getFabDate().toString(), mpsAry.getDemandQty());
                    }
                }
                break;
            case DpsVer.Type_Cell:
                List<MpsCell> list2 = mpsService.getMpsCell(ver);
                for (MpsCell mpsCell : list2) {
                    String p = mpsCell.getProduct();
                    if(!StringUtils.containsIgnoreCase(p, product)) continue;
                    Integer i = map.get(p);
                    if (i == null) {
                        Map m = new HashMap();
                        m.put("product", p);
                        m.put("fab", mpsVer.getFab());
                        m.put(mpsCell.getFabDate().toString(), mpsCell.getDemandQty());
                        mapList.add(m);
                        map.put(p, mapList.size() - 1);
                    } else {
                        Map m = mapList.get(i);
                        m.put(mpsCell.getFabDate().toString(), mpsCell.getDemandQty());
                    }
                }
                break;
            case DpsVer.Type_Lcm:
                List<MpsLcm> list3 = mpsService.getMpsLcm(ver);
                for (MpsLcm mpsLcm : list3) {
                    String p = mpsLcm.getProduct();
                    if(!StringUtils.containsIgnoreCase(p, product)) continue;
                    Integer i = map.get(p);
                    if (i == null) {
                        Map m = new HashMap();
                        m.put("product", p);
                        m.put("fab", mpsVer.getFab());
                        m.put("project", mpsLcm.getProject());
                        m.put(mpsLcm.getFabDate().toString(), mpsLcm.getDemandQty());
                        mapList.add(m);
                        map.put(p, mapList.size() - 1);
                    } else {
                        Map m = mapList.get(i);
                        m.put(mpsLcm.getFabDate().toString(), mpsLcm.getDemandQty());
                    }
                }
                break;
        }
        return ResultUtil.successPage(mapList, mapList.size());
    }

    /**
     * MC手动Excel上传MPS
     * @param file 文件
     * @return Result
     */
    @ApiOperation("MC手动Excel上传DPS")
    @ApiImplicitParam(name="file", value = "上传的excel", required = true)
    @PostMapping(value = "/importMps", headers = "content-type=multipart/form-data")
    public Result importMps(@RequestParam("file") MultipartFile file) {
        try {
            //IVO文件解密
            byte[] bytes = IVODecryptionUtils.decrypt(file.getInputStream());
            InputStream inputStream = new ByteArrayInputStream(bytes);
            mpsService.importMpsLcm(inputStream, file.getOriginalFilename(), "SYS");
        } catch (IOException e) {
            return ResultUtil.error("上传失败：" + e.getMessage());
        } catch (DecryptException e) {
            e.printStackTrace();
        }
        return ResultUtil.success();
    }


    @ApiOperation("MPS导入模板下载")
    @ApiImplicitParam(name = "plant", value = "LCM/CELL/ARY", required = true)
    @GetMapping("/exportMpsDemo")
    public void exportMpsDemo(String plant) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        Row row0 = sheet.createRow(0);
        String[] titleItems = new String[] {"机种"};
        int i=0;
        for(; i<titleItems.length; i++) {
            Cell cell = row0.createCell(i);
            cell.setCellValue(titleItems[i]);
            CellRangeAddress region = new CellRangeAddress(0, 1, i, i);
            sheet.addMergedRegion(region);
        }
        if(StringUtils.equals(plant, "LCM")) {
            row0.createCell(i++).setCellValue("2020年11月");
            row0.createCell(i++);
            row0.createCell(i++);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, i-3, i-1));
            row0.createCell(i++).setCellValue("2020年12月");
            row0.createCell(i++);
            row0.createCell(i++);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, i-3, i-1));
            Row row1 = sheet.createRow(1);
            int j=0;
            for(; j<titleItems.length; j++) {
                row1.createCell(j);
            }
            row1.createCell(j++).setCellValue("LCM1");
            row1.createCell(j++).setCellValue("LCM2");
            row1.createCell(j++).setCellValue("LCM1");
            row1.createCell(j++).setCellValue("LCM2");
        } else {
            row0.createCell(i++).setCellValue("2020年11月");
            row0.createCell(i++);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, i-2, i-1));
            row0.createCell(i++).setCellValue("2020年12月");
            row0.createCell(i++);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, i-2, i-1));
            Row row1 = sheet.createRow(1);
            int j=0;
            for(; j<titleItems.length; j++) {
                row1.createCell(j);
            }
            row1.createCell(j++).setCellValue(plant);
            row1.createCell(j++).setCellValue(plant);
        }

        HttpServletResponse response = HttpServletUtil.getResponse();
        response.setContentType("application/vnd.ms-excel;chartset=utf-8");
        String fileName = "MPS导入模板" + plant;
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
