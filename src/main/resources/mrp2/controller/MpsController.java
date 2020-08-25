package com.ivo.mrp2.controller;

import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;
import com.ivo.common.utils.*;
import com.ivo.common.utils.DateUtil;
import com.ivo.core.decryption.DecryptException;
import com.ivo.core.decryption.IVODecryptionUtils;
import com.ivo.mrp2.entity.DpsData;
import com.ivo.mrp2.entity.DpsVer;
import com.ivo.mrp2.entity.MpsData;
import com.ivo.mrp2.entity.MpsVer;
import com.ivo.mrp2.repository.MpsDataRepository;
import com.ivo.mrp2.repository.MpsVerRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc.HYPHEN;

/**
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/mps")
@Api(tags = "MPS接口")
public class MpsController {

    private MpsVerRepository mpsVerRepository;
    private MpsDataRepository mpsDataRepository;

    @Autowired
    public MpsController(MpsVerRepository mpsVerRepository, MpsDataRepository mpsDataRepository) {
        this.mpsVerRepository = mpsVerRepository;
        this.mpsDataRepository = mpsDataRepository;
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
            row0.createCell(i++).setCellValue("2020年07月");
            row0.createCell(i++);
            row0.createCell(i++);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, i-3, i-1));
            row0.createCell(i++).setCellValue("2020年08月");
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
            row1.createCell(j++).setCellValue("备料");
            row1.createCell(j++).setCellValue("LCM1");
            row1.createCell(j++).setCellValue("LCM2");
            row1.createCell(j++).setCellValue("备料");
        } else {
            row0.createCell(i++).setCellValue("2020年07月");
            row0.createCell(i++);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, i-2, i-1));
            row0.createCell(i++).setCellValue("2020年08月");
            row0.createCell(i++);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, i-2, i-1));
            Row row1 = sheet.createRow(1);
            int j=0;
            for(; j<titleItems.length; j++) {
                row1.createCell(j);
            }
            row1.createCell(j++).setCellValue(plant);
            row1.createCell(j++).setCellValue("备料");
            row1.createCell(j++).setCellValue(plant);
            row1.createCell(j++).setCellValue("备料");
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

    /**
     * MC手动Excel上传DPS
     * @param file 文件
     * @return Result
     */
    @ApiOperation("MC手动Excel上传MPS")
    @ApiImplicitParams({
            @ApiImplicitParam(name="file", value = "上传的excel", required = true),
            @ApiImplicitParam(name="plant", value = "厂别", required = true),
    })
    @PostMapping(value = "/importMps", headers = "content-type=multipart/form-data")
    public Result importMps(@RequestParam("file") MultipartFile file) {
        try {
            //IVO文件解密
            byte[] bytes = IVODecryptionUtils.decrypt(file.getInputStream());
            InputStream inputStream = new ByteArrayInputStream(bytes);
            List<List<Object>> list = ExcelUtil.readExcelFirstSheet(inputStream, file.getOriginalFilename());
            if(list == null || list.size() == 0) {
                throw new RuntimeException("excel无数据");
            }
            List<MpsData> lcm1 = new ArrayList<>();
            List<MpsData> lcm2 = new ArrayList<>();
            List<MpsData> cell = new ArrayList<>();
            List<MpsData> ary = new ArrayList<>();
            String lcm1Ver="";
            String lcm2Ver="";
            String cellVer="";
            String aryVer="";
            int rowInt = 0;int colInt = 0;
            List row0 = list.get(rowInt);
            rowInt++;
            List row1 = list.get(rowInt);

            List<Date> dateList = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            dateList.add(null);
            for(int i=1;i<row1.size(); i++) {
                if(i>=row0.size() || row0.get(i) == null ) {
                    dateList.add(dateList.get(i-1));
                    continue;
                }
                Date fabDate;
                if(row0.get(i) instanceof BigDecimal) {
                    Calendar calendar = new GregorianCalendar(1900,0,-1);
                    java.util.Date d = calendar.getTime();
                    java.util.Date dd = DateUtils.addDays(d, Integer.valueOf( (row0.get(i)).toString() ));
                    fabDate = new Date(dd.getTime());
                } else {
                    String date = (String) row0.get(i);
                    if(StringUtils.isEmpty(date)) {
                        dateList.add(dateList.get(i-1));
                        continue;
                    }
                    if (date == null) {
                        dateList.add(dateList.get(i - 1));
                        continue;
                    }
                    fabDate = new Date(sdf.parse(date).getTime());
                }
                dateList.add(fabDate);
            }

            rowInt++;
            for(;rowInt<list.size(); rowInt++) {
                List row = list.get(rowInt);
                int j=0;
                String product = (String) row.get(j);
                j++;
                for(;j<row.size(); j++) {
                    String plant = (String) row1.get(j);
                    Date fabDate;
                    Double qty;
                    if(StringUtils.equalsAnyIgnoreCase(plant, "LCM1", "LCM2")) {
                        fabDate = dateList.get(j);
                        if(row.get(j) == null) {
                            qty = null;
                        } else {
                            qty = ((BigDecimal)row.get(j)).doubleValue();
                        }
                    } else if(StringUtils.equalsAnyIgnoreCase(plant, "CELL", "ARY")) {
                        fabDate = dateList.get(j);
                        if(row.get(j) == null) {
                            qty = null;
                        } else {
                            qty = ((BigDecimal)row.get(j)).doubleValue();
                        }
                    } else {
                        continue;
                    }
                    if(qty == null || qty == 0) continue;
                    MpsData mpsData = new MpsData();
                    mpsData.setFabDate(fabDate);
                    mpsData.setProduct(product);
                    mpsData.setQty(qty);
                    if(plant.equals("LCM1")) {
                        if(StringUtils.isEmpty(lcm1Ver)) {
                            lcm1Ver = generateDpsVer()+"LCM1";
                        }
                        mpsData.setVer(lcm1Ver);
                        lcm1.add(mpsData);
                    } else if(plant.equals("LCM2")) {
                        if(StringUtils.isEmpty(lcm2Ver)) {
                            lcm2Ver = generateDpsVer()+"LCM2";
                        }
                        mpsData.setVer(lcm2Ver);
                        lcm2.add(mpsData);
                    } else if(plant.equals("CELL")) {
                        if(StringUtils.isEmpty(cellVer)) {
                            cellVer = generateDpsVer()+"CELL";
                        }
                        mpsData.setVer(cellVer);
                        cell.add(mpsData);

                    } else if(plant.equals("ARY")) {
                        if(StringUtils.isEmpty(aryVer)) {
                            aryVer = generateDpsVer()+"ARY";
                        }
                        mpsData.setVer(aryVer);
                        ary.add(mpsData);
                    }
                }
            }

            Date startDate = dateList.get(1);
            Date endDate = dateList.get(dateList.size()-1);
            if(lcm1.size()>0) {
                MpsVer mpsVer = new MpsVer();
                mpsVer.setVer(lcm1Ver);
                mpsVer.setFab("LCM1");
                mpsVer.setSource("MC导入");
                mpsVer.setStartDate(startDate);
                mpsVer.setEndDate(endDate);
                mpsVer.setCreateDate(new java.util.Date());
                mpsVerRepository.save(mpsVer);
                mpsDataRepository.saveAll(lcm1);
            }
            if(lcm2.size()>0) {
                MpsVer mpsVer = new MpsVer();
                mpsVer.setVer(lcm2Ver);
                mpsVer.setFab("LCM2");
                mpsVer.setSource("MC导入");
                mpsVer.setStartDate(startDate);
                mpsVer.setEndDate(endDate);
                mpsVer.setCreateDate(new java.util.Date());
                mpsVerRepository.save(mpsVer);
                mpsDataRepository.saveAll(lcm2);
            }
            if(cell.size()>0) {
                MpsVer mpsVer = new MpsVer();
                mpsVer.setVer(cellVer);
                mpsVer.setFab("CELL");
                mpsVer.setSource("MC导入");
                mpsVer.setStartDate(startDate);
                mpsVer.setEndDate(endDate);
                mpsVer.setCreateDate(new java.util.Date());
                mpsVerRepository.save(mpsVer);
                mpsDataRepository.saveAll(cell);
            }
            if(ary.size()>0) {
                MpsVer mpsVer = new MpsVer();
                mpsVer.setVer(aryVer);
                mpsVer.setFab("ARY");
                mpsVer.setSource("MC导入");
                mpsVer.setStartDate(startDate);
                mpsVer.setEndDate(endDate);
                mpsVer.setCreateDate(new java.util.Date());
                mpsVerRepository.save(mpsVer);
                mpsDataRepository.saveAll(ary);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DecryptException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ResultUtil.success();
    }

    public String generateDpsVer() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return  sdf.format(new java.util.Date());
    }

    @ApiOperation("获取MPS版本数据")
    @GetMapping("/getMpsVer")
    public PageResult getMpsVer() {
        List list = mpsVerRepository.findAll();
        return ResultUtil.successPage(list, list.size());
    }

    @ApiOperation("获取MPS的日期区间日历")
    @ApiImplicitParam(name = "ver", value = "DPS版本", required = true)
    @GetMapping("/getMpsCalendar")
    public Result getMpsCalendar(@RequestParam String ver) throws ParseException {
        MpsVer mpsVer = mpsVerRepository.findById(ver).orElse(null);
        Map<String, Object> map = new HashMap<>();
        List<String> months = DateUtil.getMonthBetween(mpsVer.getStartDate(), mpsVer.getEndDate());
        List<Date> days = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        for(String month : months) {
            days.add(new Date(sdf.parse(month).getTime()));
        }
        map.put("months", months);
        map.put("days", days);
        return ResultUtil.success(map);
    }

    @ApiOperation("获取MPS某版本的数据")
    @ApiImplicitParam(name = "ver", value = "MPS版本", required = true)
    @GetMapping("/getDps")
    public PageResult getMps(@RequestParam String ver) {
        MpsVer mpsVer = mpsVerRepository.findById(ver).orElse(null);
        List<MpsData> mpsDataList = mpsDataRepository.findByVer(ver);
        Map<String, Map> productMap = new HashMap<>();
        for(MpsData mpsData : mpsDataList) {
            Map map = productMap.get(mpsData.getProduct());
            if(map==null) {
                map = new HashMap();
                map.put("product", mpsData.getProduct());
                map.put("fab", mpsVer.getFab());
                productMap.put(mpsData.getProduct(), map);
            }
            map.put(mpsData.getFabDate().toString(), mpsData.getQty());
        }
        return ResultUtil.successPage(productMap.values(), productMap.values().size());
    }
}
