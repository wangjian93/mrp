package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.HttpServletUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.core.decryption.DecryptException;
import com.ivo.core.decryption.IVODecryptionUtils;
import com.ivo.mrp.service.SupplierService;
import io.swagger.annotations.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * @author wj
 * @version 1.0
 */
@Api(tags = "供应商接口")
@RestController
@RequestMapping("/mrp/supplier")
@Slf4j
public class SupplierController {

    private SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @ApiOperation("查询供应商信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "search", value = "搜索条件")
    })
    @GetMapping("/querySupplier")
    public Result querySupplier(@RequestParam(required = false, defaultValue = "1") int page,
                                @RequestParam(required = false, defaultValue = "50") int limit,
                                @RequestParam(required = false, defaultValue = "") String search) {
        Page p = supplierService.querySupplier(page-1, limit, search);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    @ApiOperation("Excel导出供应商信息")
    @GetMapping("/exportExcel")
    public void exportExcel() throws IOException {
        Workbook workbook = supplierService.exportExcel();
        HttpServletResponse response = HttpServletUtil.getResponse();
        response.setContentType("application/vnd.ms-excel;chartset=utf-8");
        String fileName = "供应商信息";
        fileName = URLEncoder.encode(fileName, "UTF8");
        response.setHeader("Content-Disposition", "attachment;filename="+fileName + ".xlsx");
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
    }

    @ApiOperation("Excel导入供应商信息")
    @PostMapping(value = "/importExcel", headers = "content-type=multipart/form-data")
    public Result importExcel(@ApiParam(value = "Excel文件", required = true) @RequestParam("file") MultipartFile file) {
        try {
            //IVO文件解密
            byte[] bytes = IVODecryptionUtils.decrypt(file.getInputStream());
            InputStream inputStream = new ByteArrayInputStream(bytes);
            String fileName = file.getOriginalFilename();
            supplierService.importExcel(inputStream, fileName);
        } catch (IOException e) {
            log.error("Excel导入供应商信息失败", e);
            return ResultUtil.error("Excel导入失败，文件读取异常");
        } catch (DecryptException e) {
            log.error("Excel导入供应商信息失败", e);
            return ResultUtil.error("Excel导入失败，文件解密异常");
        } catch (Exception e) {
            log.error("Excel导入供应商信息失败", e);
            return ResultUtil.error(e.getMessage());
        }
        return ResultUtil.success("Excel导入成功");
    }

    @ApiOperation("更新供应商信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "供应商ID", required = true),
            @ApiImplicitParam(name = "name", value = "供应商名称", required = true),
            @ApiImplicitParam(name = "sname", value = "供应商简称", required = true)
    })
    @PostMapping("/updateSupplier")
    public Result updateSupplier(String code, String name, String sname) {
        supplierService.updateSupplier(code, name, sname, "SYS");
        return ResultUtil.success("更新供应商信息成功");
    }

    @ApiOperation("添加供应商")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "供应商ID", required = true),
            @ApiImplicitParam(name = "name", value = "供应商名称", required = true),
            @ApiImplicitParam(name = "sname", value = "供应商简称", required = true)
    })
    @PostMapping("/addSupplier")
    public Result addSupplier(String code, String name, String sname) {
        supplierService.addSupplier(code, name, sname, "SYS");
        return ResultUtil.success("添加供应商成功");
    }



    //********* 主材 ***********//
    @ApiOperation("查询主材的供应商")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "material", value = "料号"),
            @ApiImplicitParam(name = "supplier", value = "供应商")
    })
    @GetMapping("/querySupplierMaterial")
    public Result querySupplierMaterial(@RequestParam(required = false, defaultValue = "1") int page,
                                        @RequestParam(required = false, defaultValue = "50") int limit,
                                        @RequestParam(required = false, defaultValue = "") String material,
                                        @RequestParam(required = false, defaultValue = "") String supplier) {
        Page p = supplierService.querySupplierMaterial(page-1, limit, material, supplier);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    @ApiOperation("Excel导入主材的供应商")
    @PostMapping(value = "/importExcelMaterial", headers = "content-type=multipart/form-data")
    public Result importExcelMaterial(@ApiParam(value = "Excel文件", required = true) @RequestParam("file") MultipartFile file) {
        //IVO文件解密
        byte[] bytes = new byte[0];
        try {
            bytes = IVODecryptionUtils.decrypt(file.getInputStream());
        } catch (DecryptException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream inputStream = new ByteArrayInputStream(bytes);
        String fileName = file.getOriginalFilename();
        supplierService.importSupplierMaterial(inputStream, fileName);
        return ResultUtil.success("Excel导入成功");
    }

    @SneakyThrows
    @ApiOperation("Excel导出主材的供应商")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "material", value = "料号查询"),
            @ApiImplicitParam(name = "supplier", value = "供应商查询")
    })
    @GetMapping(value = "/exportExcelMaterial")
    public void exportExcelMaterial(@RequestParam(required = false, defaultValue = "") String material,
                                      @RequestParam(required = false, defaultValue = "") String supplier) throws IOException {
        Workbook workbook = supplierService.exportSupplierMaterial(material, supplier );
        HttpServletResponse response = HttpServletUtil.getResponse();
        response.setContentType("application/vnd.ms-excel;chartset=utf-8");
        String fileName = "材料供应商信息";
        fileName = URLEncoder.encode(fileName, "UTF8");
        response.setHeader("Content-Disposition", "attachment;filename="+fileName + ".xlsx");
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
    }

    @ApiOperation("添加主材的供应商")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "material", value = "料号", required = true),
            @ApiImplicitParam(name = "supplierCode", value = "供应商ID", required = true)
    })
    @PostMapping(value = "/addSupplierMaterial")
    public Result addSupplierMaterial(String material, String supplierCode) {
        //TODO...
        return ResultUtil.success();
    }

    @ApiOperation("删除主材的供应商")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "material", value = "料号", required = true),
            @ApiImplicitParam(name = "supplierCode", value = "供应商ID", required = true)
    })
    @PostMapping(value = "/delSupplierMaterial")
    public Result delSupplierMaterial(String material, String supplierCode) {
        //TODO...
        return ResultUtil.success();
    }




    //********* 包材 ***********//
    @ApiOperation("查询包材的供应商")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "month", value = "月份", required = true),
            @ApiImplicitParam(name = "project", value = "机种")
    })
    @GetMapping("/querySupplierPackage")
    public Result querySupplierPackage(@RequestParam(required = false, defaultValue = "1") int page,
                                       @RequestParam(required = false, defaultValue = "50") int limit,
                                       @RequestParam(required = false, defaultValue = "") String project,
                                       String month) {
//        Page p = supplierService.querySupplierPackage(page-1, limit, month, project);
//        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
        return null;
    }

    @ApiOperation("Excel导入包材的供应商")
    @PostMapping(value = "/importExcelPackage", headers = "content-type=multipart/form-data")
    public Result importExcelPackage(@ApiParam(value = "Excel文件", required = true) @RequestParam("file") MultipartFile file) {
        //TODO...
        return null;
    }

    @ApiOperation("Excel导出包材的供应商")
    @ApiImplicitParam(name = "project", value = "机种")
    @GetMapping(value = "/exportExcelPackage")
    public Result exportExcelPackage( @RequestParam(required = false, defaultValue = "") String project) {
        //TODO...
        return null;
    }
}
