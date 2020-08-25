package com.ivo.mrp2.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;
import com.ivo.common.utils.ExcelUtil;
import com.ivo.common.utils.HttpServletUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.core.decryption.DecryptException;
import com.ivo.core.decryption.IVODecryptionUtils;
import com.ivo.mrp2.entity.MaterialLossRate;
import com.ivo.mrp2.service.MaterialLossRateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 物料损耗率接口
 * @author wj
 * @version 1.0
 */
@Api(tags = "物料损耗率接口")
@RestController
@RequestMapping("/mrp/lossRate")
public class MaterialLossRateController {


    private MaterialLossRateService lossRateService;

    @Autowired
    public MaterialLossRateController(MaterialLossRateService lossRateService) {
        this.lossRateService = lossRateService;
    }

    /**
     * 导入损耗率
     * @param file 文件
     * @return Result
     */
    @RequestMapping("/importLossRate")
    public Result importLossRate(@RequestParam("file") MultipartFile file) {
        try {
            //IVO文件解密
            byte[] bytes = IVODecryptionUtils.decrypt(file.getInputStream());
            InputStream inputStream = new ByteArrayInputStream(bytes);
            lossRateService.importLossRate(inputStream, file.getOriginalFilename());
        } catch (IOException e) {
            return ResultUtil.error("上传失败：" + e.getMessage());
        } catch (DecryptException e) {
            e.printStackTrace();
        }
        return ResultUtil.success();
    }

    /**
     * 导出损耗率
     * @return Result
     */
    @RequestMapping("/exportLossRateModel")
    public void exportLossRateModel() {
        Workbook workbook = new XSSFWorkbook();
        List<String> titleList = new ArrayList<>();
        titleList.add("物料组");
        titleList.add("料号");
        titleList.add("损耗率");
        titleList.add("备注");
        workbook.createSheet();
        ExcelUtil.writeDataToWorkbook(titleList, new ArrayList<>(), workbook, 0);
        HttpServletResponse response = HttpServletUtil.getResponse();
        response.setContentType("application/vnd.ms-excel;chartset=utf-8");
        String fileName = "损耗率上传模板";
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
     * 导出损耗率模板
     * @return Result
     */
    @RequestMapping("/exportLossRate")
    public void exportLossRate() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        sheet.setDefaultColumnWidth(20);

        List<String> titleList = new ArrayList<>();
        titleList.add("物料组");
        titleList.add("物料组名");
        titleList.add("料号");
        titleList.add("物料名");
        titleList.add("损耗率");
        titleList.add("有效");
        titleList.add("有效日期");
        titleList.add("失效日期");
        titleList.add("说明");

        List<List<Object>> list = new ArrayList<>();

        ExcelUtil.writeDataToWorkbook(titleList, list, workbook, 0);
        HttpServletResponse response = HttpServletUtil.getResponse();
        response.setContentType("application/vnd.ms-excel;chartset=utf-8");
        String fileName = "材料损耗率";
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
     * 分页查询材料损耗率数据
     * @param page 页数
     * @param limit 分页大小
     * @param materialGroup 物料组
     * @param material 料号
     * @return PageResult
     */
    @ApiOperation("分页查询材料损耗率数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "materialGroup", value = "物料组"),
            @ApiImplicitParam(name = "material", value = "料号"),
    })
    @GetMapping("/list")
    public PageResult list(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "50") int limit,
                           @RequestParam(defaultValue = "") String materialGroup,
                           @RequestParam(defaultValue = "") String material) {
        Page p = lossRateService.getMaterialLossRate(page-1, limit, materialGroup, material);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    /**
     * 维护损耗率
     * @param materialGroup 物料组
     * @param material 料号
     * @param lossRate 损耗率
     * @return Result
     */
    @ApiOperation("维护损耗率")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "materialGroup", value = "物料组"),
            @ApiImplicitParam(name = "material", value = "料号"),
            @ApiImplicitParam(name = "lossRate", value = "损耗率", required = true),
    })
    @PostMapping("/saveLossRate")
    public Result saveLossRate(@RequestParam(defaultValue = "") String materialGroup,
                              @RequestParam(defaultValue = "") String material,
                              double lossRate) {
        if(StringUtils.isEmpty(materialGroup) && StringUtils.isEmpty(material)) {
            return ResultUtil.error("保存失败");
        }

        if(StringUtils.isNotEmpty(material)) {
            lossRateService.saveForMaterial(material, lossRate);
        }
        else if(StringUtils.isNotEmpty(materialGroup)) {
            lossRateService.saveForMaterialGroup(materialGroup, lossRate);
        }
        return ResultUtil.success();
    }


    /**
     * 删除损耗率
     * @param ids 损耗率ID数组
     * @return Result
     */
    @ApiOperation("删除损耗率")
    @ApiImplicitParam(name = "ids", value = "损耗率ID数组")
    @PostMapping("/deleteLossRate")
    public Result deleteLossRate(String ids) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String[] idArray = objectMapper.readValue(ids, String[].class);
            if(idArray!=null && idArray.length>0) {
                for(String id : idArray) {
                    lossRateService.delLossRate(Long.valueOf(id));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultUtil.success();
    }
}
