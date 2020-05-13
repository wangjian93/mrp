//package com.ivo.mrp.service.impl;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ivo.common.utils.DateUtil;
//import com.ivo.common.utils.ExcelUtil;
//import com.ivo.mrp.entity.VenderArrivalPlan;
//import com.ivo.mrp.entity.VenderArrivalPlanDetail;
//import com.ivo.mrp.repository.VenderArrivalPlanRepository;
//import com.ivo.mrp.service.VenderArrivalPlanService;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.ss.util.CellRangeAddress;
//import org.apache.poi.xssf.usermodel.XSSFCellStyle;
//import org.apache.poi.xssf.usermodel.XSSFFont;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Example;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.math.BigDecimal;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
///**
// * @author wj
// * @version 1.0
// */
//@Service
//public class VenderArrivalPlanServiceImpl implements VenderArrivalPlanService {
//
//    private VenderArrivalPlanRepository repository;
//
//    @Autowired
//    public VenderArrivalPlanServiceImpl(VenderArrivalPlanRepository repository) {
//        this.repository = repository;
//    }
//
//    @Override
//    public List<VenderArrivalPlan> getVenderArrivalPlan(String month, String vender, String material) {
//
//        VenderArrivalPlan venderArrivalPlan = new VenderArrivalPlan();
//        if(month != null && !month.equals("")) {
//            venderArrivalPlan.setMonth(month);
//        }
//        if(vender != null && !vender.equals("")) {
//            venderArrivalPlan.setVender(vender);
//        }
//        if(material != null && !material.equals("")) {
//            venderArrivalPlan.setMaterial(material);
//        }
//        Example<VenderArrivalPlan> example = Example.of(venderArrivalPlan);
//        return repository.findAll(example);
//    }
//
//    @Override
//    public String importVenderArrivalPlanByMonth(InputStream inputStream, String fileName) throws Exception {
//        String month;
//        String[] propertyTypes = {"vender", "model", "vender_material", "material", "quantityType"};
//        String[] quantityTypes = {"pp", "fcs_ive", "fcst_ivo", "act_ive", "act_ivo"};
//        List<List<Object>> list = null;
//        try {
//
//
//            if (fileName.endsWith("xls")) {
//                list =  ExcelUtil.readXlsFirstSheet(inputStream);
//            }
//            else if (fileName.endsWith("xlsx")) {
//                list =  ExcelUtil.readXlsxFirstSheet(inputStream);
//            }
//            else {
//                throw new IOException("文件类型错误");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("excel读取失败");
//            throw new RuntimeException("excel读取失败," + e.getMessage(), e);
//        }
//        if(list == null || list.size() == 0) {
//            throw new RuntimeException("excel无数据");
//        }
//
//        int rowInt = 0;
//        int colInt = 0;
//        try {
//            // 解析第一行
//            List<Object> firstRow = list.get(0);
//            // 日期数组
//            Date[] dates = new Date[firstRow.size()];
//            for(;colInt<firstRow.size(); colInt++) {
//                if(colInt<propertyTypes.length) {
//                    continue;
//                }
//                Object object = firstRow.get(colInt);
//                if(object instanceof String) {
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
//                    String dateStr = (String) object;
//                    dateStr =  dateStr.trim().replace("/", "-");
//                    dates[colInt] = sdf.parse(dateStr);
//                } else {
//                    dates[colInt] = (Date) object;
//                }
//            }
//
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//            month = sdf.format(dates[propertyTypes.length]);
//
//            colInt = 0;
//            rowInt++;
//
//            // 第二行为星期几，跳过解析
//            rowInt++;
//
//            // 解析后面数据
//            String[] propertyValues = new String[propertyTypes.length];
//            Map<Date, Map> map = new HashMap<>();
//            int l=0;
//            for(;rowInt<list.size(); rowInt++) {
//                List row = list.get(rowInt);
//                // 前面列
//                for(; colInt<propertyTypes.length; colInt++) {
//                    Object obj = row.get(colInt);
//                    String value;
//                    if(obj instanceof String) {
//                        value = (String) obj;
//                    } else {
//                        value = obj.toString();
//                    }
//                    if(StringUtils.isNoneEmpty(value)) {
//                        propertyValues[colInt] = value;
//                    }
//                }
//                // 后面列
//                for(; colInt<row.size(); colInt++) {
//                    Object value = row.get(colInt);
//                    Double d;
//                    if(value == null) {
//                        continue;
//                    }
//                    if(value instanceof String) {
//                        if(StringUtils.isEmpty((String)value)) {
//                            continue;
//                        }
//                        d = Double.valueOf((String) value);
//                    } else {
//                        d = ((BigDecimal) value).doubleValue();
//                    }
//
//                    Map<String, Double> subMap = map.get(dates[colInt]);
//                    if(subMap == null) {
//                        subMap = new HashMap();
//                        map.put(dates[colInt], subMap);
//                    }
//                    subMap.put(quantityTypes[l], d);
//                }
//
//
//                l++;
//                if(l == quantityTypes.length) {
//                    ObjectMapper mapper = new ObjectMapper();
//                    String string = mapper.writeValueAsString(map);
//                    System.out.println(string);
//                    l=0;
//
//                    // 保存
//                    Map<String, String> m = new HashMap<>();
//                    for(int i=0; i<propertyTypes.length; i++) {
//                        m.put(propertyTypes[i], propertyValues[i]);
//                    }
//                    VenderArrivalPlan venderArrivalPlan = new VenderArrivalPlan();
//                    venderArrivalPlan.setVender(m.get("vender"));
//                    venderArrivalPlan.setModel(m.get("model"));
//                    venderArrivalPlan.setVender_material(m.get("vender_material"));
//                    venderArrivalPlan.setMaterial(m.get("material"));
//                    venderArrivalPlan.setMonth(month);
//
//                    List<VenderArrivalPlanDetail> venderArrivalPlanDetailList = new ArrayList<>();
//                    for (Date key : map.keySet()) {
//                        Map<String, Double> subMap = map.get(key);
//                        VenderArrivalPlanDetail venderArrivalPlanDetail = new VenderArrivalPlanDetail();
//                        venderArrivalPlanDetail.setDate(key);
//                        venderArrivalPlanDetail.setPp(subMap.get("pp"));
//                        venderArrivalPlanDetail.setFcst_ive(subMap.get("fcs_ive"));
//                        venderArrivalPlanDetail.setFcst_ivo(subMap.get("fcst_ivo"));
//                        venderArrivalPlanDetail.setAct_ive(subMap.get("act_ive"));
//                        venderArrivalPlanDetail.setAct_ivo(subMap.get("act_ivo"));
//
//                        venderArrivalPlanDetail.setVenderArrivalPlan(venderArrivalPlan);
//                        venderArrivalPlanDetailList.add(venderArrivalPlanDetail);
//                    }
//                    venderArrivalPlan.setVenderArrivalPlanDetailList(venderArrivalPlanDetailList);
//
//                    repository.deleteAll(getVenderArrivalPlan(month, m.get("vender"), m.get("material")));
//                    repository.save(venderArrivalPlan);
//                    map.clear();
//                }
//
//                colInt = 0;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("上传失败，Excel格式不准确，第"+rowInt+"行第"+colInt+"列");
//            String msg = "上传失败，Excel格式不准确，第"+rowInt+"行第"+colInt+"列";
//            throw new RuntimeException(msg, e);
//        }
//        return month;
//    }
//
//    @Override
//    public XSSFWorkbook exportVenderArrivalPlan(String month, String vender, String material) {
//        List<VenderArrivalPlan> venderArrivalPlanList = getVenderArrivalPlan(month, vender, material);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date monthDate;
//        try {
//            monthDate = sdf.parse(month+"-01");
//        } catch (ParseException e) {
//            e.printStackTrace();
//            throw new RuntimeException("选择的月份格式不对");
//        }
//        List<Date> dateList = DateUtil.getMonthEveryDays(monthDate);
//        String[] weeks = DateUtil.getWeekDay(dateList);
//
//        XSSFWorkbook wk = new XSSFWorkbook();
//        CellStyle cellStyle = buildHeaderStyle(wk);
//        Sheet sheet = wk.createSheet(month);
//
//        // 设置列宽
//        sheet.setColumnWidth(0, 15*256);
//        sheet.setColumnWidth(1, 15*256);
//        sheet.setColumnWidth(2, 15*256);
//        sheet.setColumnWidth(3, 15*256);
//        sheet.setColumnWidth(4, 20*256);
//
//        // 日期格式
//        CellStyle cellDateStyle = buildHeaderStyle(wk);
//        CreationHelper createHelper = wk.getCreationHelper();
//        cellDateStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d"));
//
//        String[] propertyTypes = {"Vender", "Model", "厂商P/N", "P/N", ""};
//        String[] quantityTypes = {"PP", "ETA IVE(FCST)", "ETA IVO(FCST)", "CVTW ACT-IVE", "CVTW ACT-IVO"};
//
//        // 设置表头
//        int rowInt = 0;
//        int colInt = 0;
//        Row row = sheet.createRow(rowInt++);
//        Cell cell = null;
//        for(int i=0; i<propertyTypes.length; i++) {
//            cell = row.createCell(colInt++);
//            cell.setCellValue(propertyTypes[i]);
//            cell.setCellStyle(cellStyle);
//        }
//        for(int i=0; i<dateList.size(); i++) {
//            cell = row.createCell(colInt++);
//            cell.setCellValue(dateList.get(i));
//            cell.setCellStyle(cellDateStyle);
//        }
//        colInt=0;
//        // 第二行设置星期几
//        row = sheet.createRow(rowInt++);
//        for(int i=0; i<propertyTypes.length; i++) {
//            cell = row.createCell(colInt++);
//            cell.setCellValue("");
//            cell.setCellStyle(cellStyle);
//        }
//        for(int i=0; i<weeks.length; i++) {
//            cell = row.createCell(colInt++);
//            cell.setCellValue(weeks[i]);
//            cell.setCellStyle(cellStyle);
//        }
//        colInt=0;
//
//        // 表头合并单元格
//        CellRangeAddress r1 = new CellRangeAddress(0,1, 0, 0);
//        CellRangeAddress r2 = new CellRangeAddress(0,1, 1, 1);
//        CellRangeAddress r3 = new CellRangeAddress(0,1, 2, 2);
//        CellRangeAddress r4 = new CellRangeAddress(0,1, 3, 3);
//        CellRangeAddress r5 = new CellRangeAddress(0,1, 4, 4);
//        sheet.addMergedRegion(r1);
//        sheet.addMergedRegion(r2);
//        sheet.addMergedRegion(r3);
//        sheet.addMergedRegion(r4);
//        sheet.addMergedRegion(r5);
//
//        // 内容数据
//        for(int i=0; i<venderArrivalPlanList.size(); i++) {
//            VenderArrivalPlan venderArrivalPlan = venderArrivalPlanList.get(i);
//            List<VenderArrivalPlanDetail> detailList = venderArrivalPlan.getVenderArrivalPlanDetailList();
//            HashMap<String, VenderArrivalPlanDetail> map = new HashMap();
//            for(VenderArrivalPlanDetail detail : detailList) {
//                map.put(sdf.format(detail.getDate()), detail);
//            }
//            for(int j=0; j<quantityTypes.length; j++) {
//                String quantityType = quantityTypes[j];
//                row = sheet.createRow(rowInt++);
//
//                cell = row.createCell(colInt++);
//                cell.setCellValue(venderArrivalPlan.getVender());
//                cell.setCellStyle(cellStyle);
//
//                cell = row.createCell(colInt++);
//                cell.setCellValue(venderArrivalPlan.getModel());
//                cell.setCellStyle(cellStyle);
//
//                cell = row.createCell(colInt++);
//                cell.setCellValue(venderArrivalPlan.getVender_material());
//                cell.setCellStyle(cellStyle);
//
//                cell = row.createCell(colInt++);
//                cell.setCellValue(venderArrivalPlan.getMaterial());
//                cell.setCellStyle(cellStyle);
//
//                cell = row.createCell(colInt++);
//                cell.setCellValue(quantityTypes[j]);
//                cell.setCellStyle(cellStyle);
//
//                for(int k=0; k<dateList.size(); k++) {
//                    Date date = dateList.get(k);
//                    VenderArrivalPlanDetail detail = map.get(sdf.format(date));
//                    if(detail == null) {
//                        cell = row.createCell(colInt++);
//                        cell.setCellValue("");
//                        cell.setCellStyle(cellStyle);
//                    } else {
//                        Double quantity = null;
//                        if(StringUtils.equalsIgnoreCase("PP", quantityType)) {
//                            quantity = detail.getPp();
//                        }
//                        if(StringUtils.equalsIgnoreCase("ETA IVE(FCST)", quantityType)) {
//                            quantity = detail.getFcst_ive();
//                        }
//                        if(StringUtils.equalsIgnoreCase("ETA IVO(FCST)", quantityType)) {
//                            quantity = detail.getFcst_ivo();
//                        }
//                        if(StringUtils.equalsIgnoreCase("CVTW ACT-IVE", quantityType)) {
//                            quantity = detail.getAct_ive();
//                        }
//                        if(StringUtils.equalsIgnoreCase("CVTW ACT-IVO", quantityType)) {
//                            quantity = detail.getAct_ivo();
//                        }
//
//                        if(quantity == null) {
//                            cell = row.createCell(colInt++);
//                            cell.setCellValue("");
//                            cell.setCellStyle(cellStyle);
//                        } else {
//                            cell = row.createCell(colInt++);
//                            cell.setCellValue(quantity);
//                            cell.setCellStyle(cellStyle);
//                        }
//                    }
//                }
//                colInt=0;
//            }
//
//
//
//            // 前5列合并合并单元格
//            CellRangeAddress c1 = new CellRangeAddress(rowInt-5,rowInt-1, 0, 0);
//            CellRangeAddress c2 = new CellRangeAddress(rowInt-5,rowInt-1,1, 1);
//            CellRangeAddress c3 = new CellRangeAddress(rowInt-5,rowInt-1, 2,2);
//            CellRangeAddress c4 = new CellRangeAddress(rowInt-5,rowInt-1, 3, 3);
//            sheet.addMergedRegion(c1);
//            sheet.addMergedRegion(c2);
//            sheet.addMergedRegion(c3);
//            sheet.addMergedRegion(c4);
//        }
//
//        return wk;
//    }
//
//
//    /**
//     * excel样式
//     */
//    private static XSSFCellStyle buildHeaderStyle(XSSFWorkbook workbook) {
//        XSSFCellStyle style = workbook.createCellStyle();
//        style.setAlignment(HorizontalAlignment.CENTER); //横向居中
//        style.setVerticalAlignment(VerticalAlignment.CENTER); //竖向居中
//        //设置边框
//        style.setBorderBottom(BorderStyle.THIN);
//        style.setBorderLeft(BorderStyle.THIN);
//        style.setBorderRight(BorderStyle.THIN);
//        style.setBorderTop(BorderStyle.THIN);
//        //字体样式
//        XSSFFont font = workbook.createFont();
//        font.setBold(true); //加粗
//        font.setColor(Font.COLOR_NORMAL);//黑色
//        font.setFontName("Arial");
//        font.setFontHeight(10); //字体大小
//        style.setFont(font);
//        return style;
//    }
//}
