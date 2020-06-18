package com.ivo.mrp2.controlller;

import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;
import com.ivo.common.utils.ExcelUtil;
import com.ivo.common.utils.HttpServletUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.entity.MaterialLossRate;
import com.ivo.mrp2.service.MaterialLossRateService;
import oracle.jdbc.proxy.annotation.Post;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 物料损耗率
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/lossRate")
public class MaterialLossRateController {


    private MaterialLossRateService lossRateService;

    @Autowired
    public MaterialLossRateController(MaterialLossRateService lossRateService) {
        this.lossRateService = lossRateService;
    }

    /**
     * 获取物料损耗率列表
     * @return Result
     */
    @GetMapping("/list")
    public PageResult list() {
        return ResultUtil.successPage(lossRateService.getMaterialLossRate());
    }

    /**
     * 分页查询物料损耗率
     * @param page 页数
     * @param limit 分页大小
     * @param material 料号
     * @param materialGroup 物料组
     * @param effectFlag 有效性
     * @return PageResult
     */
    public PageResult list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int limit,
                           @RequestParam(defaultValue = "") String material,
                           @RequestParam(defaultValue = "") String materialGroup,
                           @RequestParam(defaultValue = "") String effectFlag) {
        return null;
    }

    /**
     * 添加物料组或料号的损耗率
     * @param material 料号
     * @param materialGroup 物料组
     * @param lossRate 损耗率
     * @param memo 备注
     * @return Result
     */
    @PostMapping("/addLossRate")
    public Result addLossRate(@RequestParam String material, @RequestParam String materialGroup,
                              @RequestParam double lossRate, @RequestParam String memo) {
        if(StringUtils.isNotEmpty(material)) {
            lossRateService.saveMaterialLossRate(material, lossRate, memo);
            return ResultUtil.success();
        }
        if(StringUtils.isNotEmpty(materialGroup)) {
            lossRateService.saveMaterialGroupLossRate(materialGroup, lossRate, memo);
            return ResultUtil.success();
        }
        return ResultUtil.error("物料组或料号不能为空！");
    }

    /**
     * 废止损耗率
     * @param ids ID数组
     * @return Result
     */
    @PostMapping("/abolishLossRate")
    public Result abolishLossRate(@RequestParam(value = "ids[]") long[] ids) {
        lossRateService.abolishLossRate(ids);
        return ResultUtil.success();
    }

    /**
     * 导入损耗率
     * @param file 文件
     * @return Result
     */
    @RequestMapping("/importLossRate")
    public Result importLossRate(@RequestParam("file") MultipartFile file) {
        try {
            lossRateService.importLossRate(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            return ResultUtil.error("上传失败：" + e.getMessage());
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
        workbook.createSheet();

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

        List<MaterialLossRate> materialLossRateList = lossRateService.getMaterialLossRate();
        List<List<Object>> list = new ArrayList<>();
        for(MaterialLossRate materialLossRate : materialLossRateList) {
            List<Object> subList = new ArrayList<>();
            subList.add(materialLossRate.getMaterialGroup());
            subList.add(materialLossRate.getMaterialGroup());
            subList.add(materialLossRate.getMaterial());
            subList.add(materialLossRate.getMaterialName());
            subList.add(materialLossRate.getLossRate());
            subList.add(materialLossRate.isEffectFlag() ? "是" : "否");
            subList.add(materialLossRate.getEffectDate());
            subList.add(materialLossRate.getExpireDate());
            subList.add(materialLossRate.getMemo());
            list.add(subList);
        }

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
}
