package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.HttpServletUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.entity.direct.ary.MrpAryMaterial;
import com.ivo.mrp.entity.direct.cell.MrpCellMaterial;
import com.ivo.mrp.entity.direct.lcm.MrpLcmMaterial;
import com.ivo.mrp.service.MrpService;
import com.ivo.mrp.service.MrpWarnService;
import com.ivo.mrp.service.RunMrpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MRP数据接口
 * @author wj
 * @version 1.0
 */
@Api(tags = "MRP数据接口")
@RestController
@RequestMapping("/mrp/mrp")
public class MrpController {

    private MrpService mrpService;

    private RunMrpService runMrpService;

    private MrpWarnService mrpWarnService;

    @Autowired
    public MrpController(MrpService mrpService,  RunMrpService runMrpService, MrpWarnService mrpWarnService) {
        this.mrpService = mrpService;
        this.runMrpService = runMrpService;
        this.mrpWarnService = mrpWarnService;
    }

    @ApiOperation("查询MRP版本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "fab", value = "查询厂别"),
            @ApiImplicitParam(name = "type", value = "查询类型"),
            @ApiImplicitParam(name = "ver", value = "查询版本")
    })
    @GetMapping("/queryMrpVer")
    public Result queryMrpVer(@RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "50") int limit,
                              @RequestParam(required = false, defaultValue = "") String fab,
                              @RequestParam(required = false, defaultValue = "") String type,
                              @RequestParam(required = false, defaultValue = "") String ver) {
        Page p = mrpService.queryMrpVer(page-1, limit, fab, type, ver);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    @ApiOperation("删除MRP版本")
    @ApiImplicitParam(name = "ver", value = "Mrp版本", required = true)
    @GetMapping("/delMrpVer")
    public Result delMrpVer(String ver) {
        mrpService.delMrpVer(ver);
        return ResultUtil.success("MRP版本删除成功");
    }

    @ApiOperation("获取MRP版本的日历")
    @ApiImplicitParam(name = "ver", value = "Mrp版本", required = true)
    @GetMapping("/getMrpCalendar")
    public Result getMrpCalendar(String ver) {
        List list = mrpService.getMrpCalendar(ver);
        List<String> days = DateUtil.format_(list);
        String[] weeks = DateUtil.getWeekDay_(list);
        List<String> months = DateUtil.getMonthBetween((Date) list.get(0), (Date) list.get(list.size()-1));
        Map<String, Object> map = new HashMap<>();
        map.put("days", days);
        map.put("weeks", weeks);
        map.put("months", months);
        return ResultUtil.success(map);
    }

    @ApiOperation("获取MRP版本数据")
    @ApiImplicitParam(name = "ver", value = "Mrp版本", required = true)
    @GetMapping("/getMrpData")
    public Result getMrpData(String ver) {
        List list = mrpService.getMrpDate(ver);
        return ResultUtil.success(list);
    }


    @ApiOperation("分页获取LCM的MRP料号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "ver", value = "MRP版本", required = true),
            @ApiImplicitParam(name = "searchProduct", value = "查询机种"),
            @ApiImplicitParam(name = "searchMaterialGroup", value = "查询物料组"),
            @ApiImplicitParam(name = "searchMaterial", value = "查询料号"),
            @ApiImplicitParam(name = "searchSupplier", value = "查询供应商")
    })
    @GetMapping("/getPageMrpLcmMaterial")
    public Result getPageMrpLcmMaterial(@RequestParam(required = false, defaultValue = "1") int page,
                                        @RequestParam(required = false, defaultValue = "50") int limit,
                                        String ver,
                                        @RequestParam(required = false, defaultValue = "") String searchProduct,
                                        @RequestParam(required = false, defaultValue = "") String searchMaterialGroup,
                                        @RequestParam(required = false, defaultValue = "") String searchMaterial,
                                        @RequestParam(required = false, defaultValue = "") String searchSupplier) {
        Page<MrpLcmMaterial> p = mrpService.getPageMrpLcmMaterial(page-1, limit, ver, searchProduct, searchMaterialGroup, searchMaterial, searchSupplier);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    @ApiOperation("获取LCM料号的MRP数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ver", value = "MRP版本", required = true),
            @ApiImplicitParam(name = "material", value = "料号", required = true)
    })
    @GetMapping("/getMrpLcm")
    public Result getMrpLcm(String ver, String material) {
        List list =  mrpService.getMrpLcm(ver, material);
        return ResultUtil.success(list);
    }

    @ApiOperation("更新料号的MRP")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ver", value = "MRP版本", required = true),
            @ApiImplicitParam(name = "material", value = "料号", required = true)
    })
    @GetMapping("/updateMrpMaterial")
    public Result updateMrpMaterial(String ver, String material) {
        runMrpService.updateMrpMaterial(ver, material);
        return ResultUtil.success("MRP更新料号成功");
    }

    @ApiOperation("修改料号的结余量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ver", value = "MRP版本", required = true),
            @ApiImplicitParam(name = "material", value = "料号", required = true),
            @ApiImplicitParam(name = "fabDate", value = "日期", required = true),
            @ApiImplicitParam(name = "balanceQty", value = "结余量", required = true)
    })
    @GetMapping("/updateMrpBalanceQty")
    public Result updateMrpBalanceQty(String ver, String material, Date fabDate, double balanceQty) {
        runMrpService.updateMrpBalanceQty(ver, material, fabDate, balanceQty);
        runMrpService.updateMrpMaterial(ver, material);
        return ResultUtil.success("MRP更新料号成功");
    }

    @ApiOperation("更新MRP的供应商分配数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ver", value = "MRP版本", required = true),
            @ApiImplicitParam(name = "material", value = "料号", required = true),
            @ApiImplicitParam(name = "fabDate", value = "日期", required = true)
    })
    @GetMapping("/updateMrpAllocationQty")
    public Result updateMrpAllocationQty(String ver, String material, Date fabDate) {
        double allocationQty = runMrpService.updateMrpAllocationQty(ver, material, fabDate);
        return ResultUtil.success("MRP更新料号成功", allocationQty);
    }



    @ApiOperation("分页获取CELL的MRP料号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "ver", value = "MRP版本", required = true),
            @ApiImplicitParam(name = "searchProduct", value = "查询机种"),
            @ApiImplicitParam(name = "searchMaterialGroup", value = "查询物料组"),
            @ApiImplicitParam(name = "searchMaterial", value = "查询料号"),
            @ApiImplicitParam(name = "searchSupplier", value = "查询供应商")
    })
    @GetMapping("/getPageMrpCellMaterial")
    public Result getPageMrpCellMaterial(@RequestParam(required = false, defaultValue = "1") int page,
                                        @RequestParam(required = false, defaultValue = "50") int limit,
                                        String ver,
                                        @RequestParam(required = false, defaultValue = "") String searchProduct,
                                        @RequestParam(required = false, defaultValue = "") String searchMaterialGroup,
                                        @RequestParam(required = false, defaultValue = "") String searchMaterial,
                                        @RequestParam(required = false, defaultValue = "") String searchSupplier) {
        Page<MrpCellMaterial> p = mrpService.getPageMrpCellMaterial(page-1, limit, ver, searchProduct, searchMaterialGroup, searchMaterial, searchSupplier);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    @ApiOperation("获取Cell料号的MRP数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ver", value = "MRP版本", required = true),
            @ApiImplicitParam(name = "material", value = "料号", required = true)
    })
    @GetMapping("/getMrpCell")
    public Result getMrpCell(String ver, String material) {
        List list =  mrpService.getMrpCell(ver, material);
        return ResultUtil.success(list);
    }

    @ApiOperation("分页获取Ary的MRP料号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "ver", value = "MRP版本", required = true),
            @ApiImplicitParam(name = "searchProduct", value = "查询机种"),
            @ApiImplicitParam(name = "searchMaterialGroup", value = "查询物料组"),
            @ApiImplicitParam(name = "searchMaterial", value = "查询料号"),
            @ApiImplicitParam(name = "searchSupplier", value = "查询供应商")
    })
    @GetMapping("/getPageMrpAryMaterial")
    public Result getPageMrpAryMaterial(@RequestParam(required = false, defaultValue = "1") int page,
                                        @RequestParam(required = false, defaultValue = "50") int limit,
                                        String ver,
                                        @RequestParam(required = false, defaultValue = "") String searchProduct,
                                        @RequestParam(required = false, defaultValue = "") String searchMaterialGroup,
                                        @RequestParam(required = false, defaultValue = "") String searchMaterial,
                                        @RequestParam(required = false, defaultValue = "") String searchSupplier) {
        Page<MrpAryMaterial> p = mrpService.getPageMrpAryMaterial(page-1, limit, ver, searchProduct, searchMaterialGroup, searchMaterial, searchSupplier);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    @ApiOperation("获取Ary料号的MRP数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ver", value = "MRP版本", required = true),
            @ApiImplicitParam(name = "material", value = "料号", required = true)
    })
    @GetMapping("/getMrpAry")
    public Result getMrpAry(String ver, String material) {
        List list =  mrpService.getMrpAry(ver, material);
        return ResultUtil.success(list);
    }

    @GetMapping("/getCalendar")
    public Result getCalendar(Date startDate, Date endDate) {
        List<java.util.Date> list = DateUtil.getCalendar(startDate, endDate);
        List<String> days = DateUtil.format_(list);
        String[] weeks = DateUtil.getWeekDay_(list);
        List<String> months = DateUtil.getMonthBetween(list.get(0), list.get(list.size()-1));
        Map<String, Object> map = new HashMap<>();
        map.put("days", days);
        map.put("weeks", weeks);
        map.put("months", months);
        return ResultUtil.success(map);
    }

    @GetMapping("/updateMrp")
    public Result updateMrp(String ver) {
        runMrpService.updateMrp(ver);
        return ResultUtil.success("更新成功");
    }

    @ApiOperation("选择DPS/MPS版本运算MRP")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dpsVer", value = "DPS版本", required = true),
            @ApiImplicitParam(name = "mpsVer", value = "MPS版本")
    })
    @PostMapping("/runMrp")
    public Result runMrp(String dpsVer, String mpsVer) throws IOException, ClassNotFoundException {
        String[] dpsVers = new String[]{};
        if(StringUtils.isNotEmpty(dpsVer)) {
            dpsVers = dpsVer.split(",");
        }
        String[] mpsVers = new String[]{};
        if(StringUtils.isNotEmpty(mpsVer)) {
            mpsVers = mpsVer.split(",");
        }
        runMrpService.runMrp(dpsVers, mpsVers, "SYS");
        return ResultUtil.success();
    }

    @ApiOperation("Excel导出MRP数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ver", value = "MRP版本", required = true),
            @ApiImplicitParam(name = "product", value = "查询机种"),
            @ApiImplicitParam(name = "materialGroup", value = "查询物料组"),
            @ApiImplicitParam(name = "material", value = "查询料号"),
            @ApiImplicitParam(name = "supplier", value = "查询供应商")
    })
    @GetMapping("/exportExcel")
    public void exportExcel(String ver,
                          @RequestParam(required = false, defaultValue = "") String product,
                          @RequestParam(required = false, defaultValue = "") String materialGroup,
                          @RequestParam(required = false, defaultValue = "") String material,
                          @RequestParam(required = false, defaultValue = "") String supplier) {
        Workbook workbook = mrpService.exportMrp(ver, product, materialGroup, material, supplier);
        HttpServletResponse response = HttpServletUtil.getResponse();
        response.setContentType("application/vnd.ms-excel;chartset=utf-8");
        String fileName = "MRP_"+ver;
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

    @ApiOperation("Excel导出MRP数据")
    @GetMapping("/getMrpWarn")
    public Result getWarn(String ver) {
        List list = mrpWarnService.getMrpWarn(ver);
        return ResultUtil.successPage(list, list.size());
    }
}
