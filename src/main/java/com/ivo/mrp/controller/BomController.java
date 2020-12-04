package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.HttpServletUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.core.decryption.DecryptException;
import com.ivo.core.decryption.IVODecryptionUtils;
import com.ivo.mrp.service.BomPackageLcmService;
import com.ivo.mrp.service.BomPackageService;
import com.ivo.mrp.service.BomPolService;
import com.ivo.mrp.service.BomService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Api(tags = "BOM接口")
@RestController
@RequestMapping("/mrp/bom")
@Slf4j
public class BomController {

    private BomService bomService;
    private BomPackageService bomPackageService;
    private BomPolService bomPolService;

    private BomPackageLcmService bomPackageLcmService;

    @Autowired
    public BomController(BomService bomService,BomPackageService bomPackageService, BomPolService bomPolService,
                         BomPackageLcmService bomPackageLcmService) {
        this.bomService = bomService;
        this.bomPackageService = bomPackageService;
        this.bomPolService = bomPolService;
        this.bomPackageLcmService = bomPackageLcmService;
    }

    //** 主材 **//

    @ApiOperation("查询主材Bom的机种")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "fab", value = "厂别", required = true),
            @ApiImplicitParam(name = "product", value = "查询机种")
    })
    @GetMapping("/queryBomProduct")
    public Result queryBomProduct(@RequestParam(required = false, defaultValue = "1") int page,
                                  @RequestParam(required = false, defaultValue = "50") int limit,
                                  String fab,
                                  @RequestParam(required = false, defaultValue = "") String product) {
        Page p = bomService.queryProduct(page-1, limit, fab, product);
        if(p!=null) {
            return ResultUtil.successPage(p.getContent(), p.getTotalElements());
        } else {
            return ResultUtil.successPage(new ArrayList(), 0);
        }
    }

    @ApiOperation("获取主材的机种Bom")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fab", value = "厂别", required = true),
            @ApiImplicitParam(name = "product", value = "机种", required = true)
    })
    @GetMapping("/getBom")
    public Result getBom(String fab, String product) {
        List list = new ArrayList<>();
        if(StringUtils.equalsAnyIgnoreCase(fab, "LCM1", "LCM2")) {
            list = bomService.getLcmBom(product, fab);
        } else if(StringUtils.equalsIgnoreCase(fab, "ARY")) {
            list = bomService.getAryBom(product);
        } else if(StringUtils.equalsIgnoreCase(fab, "CELL")) {
            list = bomService.queryCellBom(product);
        }
        return ResultUtil.success(list);
    }

    //** 包材 **//

    @ApiOperation("获取包材的机种Bom")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "product", value = "机种", required = true),
            @ApiImplicitParam(name = "type", value = "切片类型", required = true),
            @ApiImplicitParam(name = "linkQty", value = "切片数"),
            @ApiImplicitParam(name = "mode", value = "切单模式")
    })
    @GetMapping("/getBomPackage")
    public Result getBomPackage(String product, String type, @RequestParam(required = false, defaultValue = "") String linkQty, @RequestParam(required = false, defaultValue = "") String mode) {
        Double linkQty_;
        if(StringUtils.isEmpty(linkQty)) {
            linkQty_ = null;
        } else {
            linkQty_ = Double.valueOf(linkQty);
        }
        if(StringUtils.isEmpty(mode)) {
            mode = null;
        }
        return ResultUtil.success(bomPackageService.getBomPackage(product, type, linkQty_, mode));
    }

    @ApiOperation("查询包材的机种Bom")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "searchProduct", value = "查询机种")
    })
    @GetMapping("/queryBomPackage")
    public Result queryBomPackage(@RequestParam(required = false, defaultValue = "1") int page,
                                  @RequestParam(required = false, defaultValue = "50") int limit,
                                  @RequestParam(required = false, defaultValue = "") String searchProduct) {
        Page p = bomPackageService.queryBomPackage(page-1, limit, searchProduct);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    @ApiOperation("Excel导入包材BOM")
    @PostMapping(value = "/importExcel", headers = "content-type=multipart/form-data")
    public Result importExcel(@ApiParam(value = "Excel文件", required = true) @RequestParam("file") MultipartFile file) {
        try {
            //IVO文件解密
            byte[] bytes = IVODecryptionUtils.decrypt(file.getInputStream());
            InputStream inputStream = new ByteArrayInputStream(bytes);
            String fileName = file.getOriginalFilename();
            bomPackageService.importBomPackage(inputStream, fileName);
        } catch (IOException e) {
            log.error("Excel导入包材BOM失败", e);
            return ResultUtil.error("Excel导入失败，文件读取异常");
        } catch (DecryptException e) {
            log.error("Excel导入包材BOM失败", e);
            return ResultUtil.error("Excel导入失败，文件解密异常");
        } catch (Exception e) {
            log.error("Excel导入包材BOM失败", e);
            return ResultUtil.error(e.getMessage());
        }
        return ResultUtil.success("Excel导入成功");
    }

    //** POL **//

    @ApiOperation("查询POL的机种BOM")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "searchProduct", value = "查询机种")
    })
    @GetMapping("/queryBomPol")
    public Result queryBomPol(@RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "50") int limit,
                              @RequestParam(required = false, defaultValue = "") String searchProduct) {
        Page p = bomPolService.queryBomPol(page-1, limit, searchProduct);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    @ApiOperation("Excel导入POL BOM")
    @PostMapping(value = "/importBomPol", headers = "content-type=multipart/form-data")
    public Result importBomPol(@ApiParam(value = "Excel文件", required = true) @RequestParam("file") MultipartFile file) {
        try {
            //IVO文件解密
            byte[] bytes = IVODecryptionUtils.decrypt(file.getInputStream());
            InputStream inputStream = new ByteArrayInputStream(bytes);
            String fileName = file.getOriginalFilename();
            bomPolService.importBomPol(inputStream, fileName);
        } catch (IOException e) {
            log.error("Excel导入POL BOM失败", e);
            return ResultUtil.error("Excel导入失败，文件读取异常");
        } catch (DecryptException e) {
            log.error("Excel导入POL BOM失败", e);
            return ResultUtil.error("Excel导入失败，文件解密异常");
        } catch (Exception e) {
            log.error("Excel导入POL BOM失败", e);
            return ResultUtil.error(e.getMessage());
        }
        return ResultUtil.success("Excel导入成功");
    }

    @ApiOperation("Excel导出POL BOM")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "product", value = "机种")
    })
    @GetMapping("/exportBomPol")
    public void exportExcel(@RequestParam(required = false, defaultValue = "") String product) throws IOException {
        Workbook workbook = bomPolService.exportBomPol(product);
        HttpServletResponse response = HttpServletUtil.getResponse();
        response.setContentType("application/vnd.ms-excel;chartset=utf-8");
        String fileName = "POL BOM";
        fileName = URLEncoder.encode(fileName, "UTF8");
        response.setHeader("Content-Disposition", "attachment;filename="+fileName + ".xlsx");
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
    }

    @ApiOperation("Excel POL BOM模板下载")
    @GetMapping("/downloadBomPolExcel")
    public void downloadBomPolExcel() throws IOException {
        Workbook workbook = bomPolService.downloadBomPolExcel();
        HttpServletResponse response = HttpServletUtil.getResponse();
        response.setContentType("application/vnd.ms-excel;chartset=utf-8");
        String fileName = "POL BOM上传模板";
        fileName = URLEncoder.encode(fileName, "UTF8");
        response.setHeader("Content-Disposition", "attachment;filename="+fileName + ".xlsx");
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
    }




    //** LCM包材 **//
    @ApiOperation("查询LCM包材Bom的机种")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "fab", value = "厂别", required = true),
            @ApiImplicitParam(name = "product", value = "查询机种")
    })
    @GetMapping("/queryBomPackageLcmProduct")
    public Result queryBomPackageLcmProduct(@RequestParam(required = false, defaultValue = "1") int page,
                                  @RequestParam(required = false, defaultValue = "50") int limit,
                                  String fab,
                                  @RequestParam(required = false, defaultValue = "") String product) {
        Page p = bomPackageLcmService.queryProduct(page-1, limit, fab, product);
        if(p!=null) {
            return ResultUtil.successPage(p.getContent(), p.getTotalElements());
        } else {
            return ResultUtil.successPage(new ArrayList(), 0);
        }
    }

    @ApiOperation("获取LCM包材的机种Bom")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fab", value = "厂别", required = true),
            @ApiImplicitParam(name = "product", value = "机种", required = true)
    })
    @GetMapping("/getBomPackageLcm")
    public Result getBomPackageLcm(String fab, String product) {
        List list = bomPackageLcmService.getLcmPackageBom(fab, product);
        return ResultUtil.success(list);
    }

    @ApiOperation("获取LCM包材材料单独拎出计算的小材料")
    @GetMapping("/getAloneMaterial")
    public Result getAloneMaterial() {
        List list = bomPackageLcmService.getAloneMaterial();
        return ResultUtil.successPage(list, list.size());
    }

}
