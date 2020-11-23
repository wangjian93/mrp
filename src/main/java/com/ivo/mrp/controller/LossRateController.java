package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.HttpServletUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.core.decryption.DecryptException;
import com.ivo.core.decryption.IVODecryptionUtils;
import com.ivo.mrp.service.LossRateService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 物料的损耗率接口
 * @author wj
 * @version 1.0
 */
@Api(tags = "损耗率接口")
@RestController
@RequestMapping("/mrp/lossRate")
@Slf4j
public class LossRateController {

    private LossRateService lossRateService;

    @Autowired
    public LossRateController(LossRateService lossRateService) {
        this.lossRateService = lossRateService;
    }

    @ApiOperation("查询物料的损耗率")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "materialGroup", value = "查询物料组"),
            @ApiImplicitParam(name = "material", value = "查询料号")
    })
    @GetMapping("/queryLossRate")
    public Result queryLossRate(@RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "50") int limit,
                              @RequestParam(required = false, defaultValue = "") String materialGroup,
                              @RequestParam(required = false, defaultValue = "") String material) {
        Page p = lossRateService.queryLossRate(page-1, limit, materialGroup, material);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    @ApiOperation("保存料号的损耗率")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "material", value = "料号", required = true),
            @ApiImplicitParam(name = "lossRate", value = "损耗率", required = true)
    })
    @PostMapping("/saveLossRateForMaterial")
    public Result saveLossRateForMaterial(String material, double lossRate) {
        lossRateService.saveMaterialLossRate(material, lossRate, "SYS", "");
        return ResultUtil.success("保存成功");
    }

    @ApiOperation("保存物料组的损耗率")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "materialGroup", value = "物料组", required = true),
            @ApiImplicitParam(name = "lossRate", value = "损耗率", required = true)
    })
    @PostMapping("/saveLossRateForGroup")
    public Result saveLossRateForGroup(String materialGroup, double lossRate) {
        lossRateService.saveMaterialGroupLossRate(materialGroup, lossRate, "SYS");
        return ResultUtil.success("保存成功");
    }


    @ApiOperation("删除料号的损耗率")
    @ApiImplicitParam(name = "materialGroup", value = "料号", required = true)
    @PostMapping("/delLossRateForMaterial")
    public Result delLossRateForMaterial(String material) {
        lossRateService.delMaterialLossRate(material, "SYS");
        return ResultUtil.success("保存成功");
    }

    @ApiOperation("删除物料组的损耗率")
    @ApiImplicitParam(name = "materialGroup", value = "物料组", required = true)
    @PostMapping("/delLossRateForGroup")
    public Result delLossRateForGroup(String materialGroup) {
        lossRateService.delMaterialGroupLossRate(materialGroup, "SYS");
        return ResultUtil.success("保存成功");
    }

    @ApiOperation("Excel导入损耗率")
    @PostMapping(value = "/importExcel", headers = "content-type=multipart/form-data")
    public Result importExcel(@ApiParam(value = "Excel文件", required = true) @RequestParam("file") MultipartFile file) {
        try {
            //IVO文件解密
            byte[] bytes = IVODecryptionUtils.decrypt(file.getInputStream());
            InputStream inputStream = new ByteArrayInputStream(bytes);
            String fileName = file.getOriginalFilename();
            lossRateService.importExcel(inputStream, fileName);
        } catch (IOException e) {
            log.error("Excel导入损耗率失败", e);
            return ResultUtil.error("Excel导入失败，文件读取异常");
        } catch (DecryptException e) {
            log.error("Excel导入损耗率失败", e);
            return ResultUtil.error("Excel导入失败，文件解密异常");
        } catch (Exception e) {
            log.error("Excel导入损耗率失败", e);
            return ResultUtil.error(e.getMessage());
        }
        return ResultUtil.success("Excel导入成功");
    }

    @ApiOperation("Excel导出损耗率")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "materialGroup", value = "查询物料组"),
            @ApiImplicitParam(name = "material", value = "查询料号")
    })
    @GetMapping("/exportExcel")
    public void exportExcel(@RequestParam(required = false) String materialGroup,
                                      @RequestParam(required = false) String material ) throws IOException {
        Workbook workbook;
        if(StringUtils.isEmpty(materialGroup) && StringUtils.isEmpty(material)) {
            workbook = lossRateService.exportExcel();
        } else {
            workbook = lossRateService.exportExcel(materialGroup, material);
        }

        HttpServletResponse response = HttpServletUtil.getResponse();
        response.setContentType("application/vnd.ms-excel;chartset=utf-8");
        String fileName = "损耗率";
        fileName = URLEncoder.encode(fileName, "UTF8");
        response.setHeader("Content-Disposition", "attachment;filename="+fileName + ".xlsx");
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
    }

    @ApiOperation("Excel损耗率模板下载")
    @GetMapping("/downloadExcel")
    public void downloadExcel() throws IOException {
        Workbook workbook = lossRateService.downloadExcel();

        HttpServletResponse response = HttpServletUtil.getResponse();
        response.setContentType("application/vnd.ms-excel;chartset=utf-8");
        String fileName = "损耗率上传模板";
        fileName = URLEncoder.encode(fileName, "UTF8");
        response.setHeader("Content-Disposition", "attachment;filename="+fileName + ".xlsx");
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
    }
}
