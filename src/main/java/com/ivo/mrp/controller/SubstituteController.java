package com.ivo.mrp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.result.Result;
import com.ivo.common.utils.HttpServletUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.core.decryption.DecryptException;
import com.ivo.core.decryption.IVODecryptionUtils;
import com.ivo.mrp.service.SubstituteService;
import io.swagger.annotations.*;
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
import java.util.List;
import java.util.Map;

/**
 * 替代料接口
 * @author wj
 * @version 1.0
 */
@Api(tags = "替代料接口")
@RestController
@RequestMapping("/mrp/substitute")
@Slf4j
public class SubstituteController {

    private SubstituteService substituteService;

    @Autowired
    public SubstituteController(SubstituteService substituteService) {
        this.substituteService = substituteService;
    }

    @ApiOperation("查询替代料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "fab", value = "查询厂别"),
            @ApiImplicitParam(name = "product", value = "查询机种"),
            @ApiImplicitParam(name = "materialGroup", value = "查询物料组")
    })
    @GetMapping("/querySubstitute")
    public Result querySubstitute(@RequestParam(required = false, defaultValue = "1") int page,
                                @RequestParam(required = false, defaultValue = "50") int limit,
                                @RequestParam(required = false, defaultValue = "") String fab,
                                @RequestParam(required = false, defaultValue = "") String product,
                                @RequestParam(required = false, defaultValue = "") String materialGroup) {
        Page p = substituteService.querySubstitute(page-1, limit, fab, product, materialGroup);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    @ApiOperation("获取一组替代料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fab", value = "厂别", required = true),
            @ApiImplicitParam(name = "product", value = "机种", required = true),
            @ApiImplicitParam(name = "materialGroup", value = "物料组", required = true)
    })
    @GetMapping("/getSubstitute")
    public Result getSubstitute(String fab, String product, String materialGroup) {
        return ResultUtil.success(substituteService.getSubstitute(fab, product, materialGroup));
    }

    @ApiOperation("替代料保存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fab", value = "厂别", required = true),
            @ApiImplicitParam(name = "product", value = "机种", required = true),
            @ApiImplicitParam(name = "materialGroup", value = "物料组", required = true),
            @ApiImplicitParam(name = "data", value = "替代比例JSON数据", required = true)
    })
    @PostMapping("/saveSubstitute")
    public Result saveSubstitute(String fab, String product, String materialGroup, String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map> mapList;
        try {
            mapList = objectMapper.readValue(data, new TypeReference<List<Map>>() {});
        } catch (IOException e) {
            log.error("替代料保存失败", e);
            return ResultUtil.error("替代料保存失败，提交数据解析错误");
        }
        substituteService.saveSubstitute(fab, product, materialGroup, mapList, "SYS");
        return ResultUtil.success("替代料保存成功");
    }

    @ApiOperation("删除一组替代料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fab", value = "厂别", required = true),
            @ApiImplicitParam(name = "product", value = "机种", required = true),
            @ApiImplicitParam(name = "materialGroup", value = "物料组", required = true)
    })
    @PostMapping("/delSubstitute")
    public Result delSubstitute(String fab, String product, String materialGroup) {
        substituteService.delSubstitute(fab, product, materialGroup, "SYS");
        return ResultUtil.success("替代料删除成功");
    }

    @ApiOperation("Excel导入替代料")
    @PostMapping(value = "/importExcel", headers = "content-type=multipart/form-data")
    public Result importExcel(@ApiParam(value = "Excel文件", required = true) @RequestParam("file") MultipartFile file) {
        try {
            //IVO文件解密
            byte[] bytes = IVODecryptionUtils.decrypt(file.getInputStream());
            InputStream inputStream = new ByteArrayInputStream(bytes);
            String fileName = file.getOriginalFilename();
            substituteService.importExcel(inputStream, fileName);
        } catch (IOException e) {
            log.error("Excel导入替代料失败", e);
            return ResultUtil.error("Excel导入失败，文件读取异常");
        } catch (DecryptException e) {
            log.error("Excel导入替代料失败", e);
            return ResultUtil.error("Excel导入失败，文件解密异常");
        } catch (Exception e) {
            log.error("Excel导入替代料失败", e);
            return ResultUtil.error(e.getMessage());
        }
        return ResultUtil.success("Excel导入成功");
    }

    @ApiOperation("Excel导出替代料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fab", value = "厂别", required = true),
            @ApiImplicitParam(name = "product", value = "机种"),
            @ApiImplicitParam(name = "materialGroup", value = "物料组")
    })
    @GetMapping("/exportExcel")
    public void exportExcel(String fab,
                            @RequestParam(required = false, defaultValue = "") String product,
                            @RequestParam(required = false, defaultValue = "") String materialGroup) throws IOException {
        Workbook workbook = substituteService.exportExcel(fab, product, materialGroup);
        HttpServletResponse response = HttpServletUtil.getResponse();
        response.setContentType("application/vnd.ms-excel;chartset=utf-8");
        String fileName = "替代料";
        fileName = URLEncoder.encode(fileName, "UTF8");
        response.setHeader("Content-Disposition", "attachment;filename="+fileName + ".xlsx");
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
    }

    @ApiOperation("Excel替代料模板下载")
    @GetMapping("/downloadExcel")
    public void downloadExcel() throws IOException {
        Workbook workbook = substituteService.downloadExcel();
        HttpServletResponse response = HttpServletUtil.getResponse();
        response.setContentType("application/vnd.ms-excel;chartset=utf-8");
        String fileName = "替代料上传模板";
        fileName = URLEncoder.encode(fileName, "UTF8");
        response.setHeader("Content-Disposition", "attachment;filename="+fileName + ".xlsx");
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
    }
}
