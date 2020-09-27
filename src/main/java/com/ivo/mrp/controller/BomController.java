package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.core.decryption.DecryptException;
import com.ivo.core.decryption.IVODecryptionUtils;
import com.ivo.mrp.service.BomPackageService;
import com.ivo.mrp.service.BomService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @Autowired
    public BomController(BomService bomService,BomPackageService bomPackageService) {
        this.bomService = bomService;
        this.bomPackageService = bomPackageService;
    }

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
}
