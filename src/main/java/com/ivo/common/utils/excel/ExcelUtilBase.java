package com.ivo.common.utils.excel;

import com.ivo.common.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ClassName: ExcelUtilBase
 * Function:  ExcelUtil基础类
 * Date:      2019/7/13 9:55
 * author     likaixuan
 * version    V1.0
 */
public class ExcelUtilBase {
    /**
     * @Function 把传进指定格式的字符串解析到Map中
     * @author   likaixuan
     * @Date     2019-07-05 15:09
     * @param     keyValue
     * @return   java.util.Map<java.lang.String,java.lang.String>
     */
    public static Map<String, String> getMap(String keyValue) {

        Map<String, String> map = new HashMap<String, String>();
        if (keyValue != null) {
            String[] str = keyValue.split(",");
            for (String element : str) {
                String[] str2 = element.split(":");
                map.put(str2[0], str2[1]);
            }
        }
        return map;
    }

    /**
     * @Function 把传进指定格式的字符串解析到Map中
     * @author   likaixuan
     * @Date     2019-07-05 15:09
     * @param     clazz
     * @return   java.util.Map<java.lang.String,java.lang.String>
     */
    public static Map<String, String> getMap(Class<T> clazz) throws NoSuchFieldException {

        Map<String, String> map = new HashMap<String, String>();
        Field field;
        Field[] fields=clazz.getDeclaredFields();
        for (int i = 0; i <fields.length ; i++) {
            fields[i].setAccessible(true);
        }
        for (int i = 0; i <fields.length ; i++) {
            field=clazz.getDeclaredField(fields[i].getName());
            Excel column=field.getAnnotation(Excel.class);
            if(column!=null){
                map.put(column.title(),field.getName());
            }
        }
        return map;
    }


    /**
     * @Function 把传进指定格式的字符串解析到List中
     * @author   likaixuan
     * @Date     2019-07-05 15:08
     * @param    keyValue
     * @return   java.util.List<java.lang.String>
     */
    public static List<String> getList(String keyValue) {

        List<String> list = new ArrayList<String>();
        if (keyValue != null) {
            String[] str = keyValue.split(",");

            for (String element : str) {
                String[] str2 = element.split(":");
                list.add(str2[0]);
            }
        }
        return list;
    }

    /**
     * @Function 把传进指定格式的字符串解析到List中
     * @author   likaixuan
     * @Date     2019-07-05 15:08
     * @param    keyValue
     * @return   java.util.List<java.lang.String>
     */
    public static List<String> getList(Class<T> clazz) throws NoSuchFieldException {

        List<String> list = new ArrayList<String>();
        Field field;
        Field[] fields=clazz.getDeclaredFields();
        for (int i = 0; i <fields.length ; i++) {
            fields[i].setAccessible(true);
        }
        for (int i = 0; i <fields.length ; i++) {
            field=clazz.getDeclaredField(fields[i].getName());
            Excel column=field.getAnnotation(Excel.class);
            if(column!=null){
                list.add(column.title());
            }
        }
        return list;
    }

    public static List getResult(ExcelParam excelParam) throws Exception {
        Set keySet = null;
        //新加入了注解，如果map为空，则自动从class中的注解自动查找
        if(excelParam.getMap() == null || excelParam.getMap().size()==0){
            excelParam.setMap(getMap(excelParam.getClazz()));
            keySet = excelParam.getMap().keySet();
        }else {
            keySet = excelParam.getMap().keySet();// 返回键的集合
        }
        List<Object> list = new ArrayList<Object>();
        String fileType = "";
        InputStream is = null;
        Workbook wb = null;
        if(excelParam.getStream()){
            is = new ByteArrayInputStream(excelParam.getBuf());
            wb = WorkbookFactory.create(is);
        }else{
            fileType = excelParam.getFilePath().substring(excelParam.getFilePath().lastIndexOf(".") + 1, excelParam.getFilePath().length());
            is = new FileInputStream(excelParam.getFilePath());
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook(is);
            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook(is);
            } else {
                throw new Exception("您输入的excel格式不正确");
            }
        }


        int startSheetNum = 0;
        int endSheetNum = 1;
        if (null != excelParam.getSheetIndex()) {
            startSheetNum = excelParam.getSheetIndex() - 1;
            endSheetNum = excelParam.getSheetIndex();
        }
        for (int sheetNum = startSheetNum; sheetNum < endSheetNum; sheetNum++) {// 获取每个Sheet表

            int rowNum_x = -1;// 记录第x行为表头
            Map<String, Integer> cellmap = new HashMap<String, Integer>();// 存放每一个field字段对应所在的列的序号
            List<String> headlist = new ArrayList();// 存放所有的表头字段信息

            Sheet hssfSheet = wb.getSheetAt(sheetNum);

            // 设置默认最大行为50w行
            if (hssfSheet != null && hssfSheet.getLastRowNum() > 500000) {
                throw new Exception("Excel 数据超过50w行,请检查是否有空行,或分批导入");
            }

            // 循环行Row
            for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {

                if (excelParam.getRowNumIndex() != null && rowNum_x == -1) {// 如果传值指定从第几行开始读，就从指定行寻找，否则自动寻找
                    Row hssfRow = hssfSheet.getRow(excelParam.getRowNumIndex());
                    if (hssfRow == null) {
                        throw new RuntimeException("指定的行为空，请检查");
                    }
                    rowNum = excelParam.getRowNumIndex() - 1;
                }
                Row hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                boolean flag = false;
                for (int i = 0; i < hssfRow.getLastCellNum(); i++) {
                    if (hssfRow.getCell(i) != null && !("").equals(hssfRow.getCell(i).toString().trim())) {
                        flag = true;
                    }
                }
                if (!flag) {
                    continue;
                }

                if (rowNum_x == -1) {
                    // 循环列Cell
                    for (int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {

                        Cell hssfCell = hssfRow.getCell(cellNum);
                        if (hssfCell == null) {
                            continue;
                        }

                        String tempCellValue = hssfSheet.getRow(rowNum).getCell(cellNum).getStringCellValue();

                        tempCellValue = StringUtils.remove(tempCellValue, (char) 160);
                        tempCellValue = tempCellValue.trim();

                        headlist.add(tempCellValue);

                        Iterator it = keySet.iterator();

                        while (it.hasNext()) {
                            Object key = it.next();
                            if (StringUtils.isNotBlank(tempCellValue)
                                    && StringUtils.equals(tempCellValue, key.toString())) {
                                rowNum_x = rowNum;
                                cellmap.put(excelParam.getMap().get(key).toString(), cellNum);
                            }
                        }
                        if (rowNum_x == -1) {
                            throw new Exception("没有找到对应的字段或者对应字段行上面含有不为空白的行字段");
                        }
                    }

                    if(excelParam.getSameHeader()){
                        // 读取到列后，检查表头是否完全一致--start
                        for (int i = 0; i < headlist.size(); i++) {
                            boolean boo = false;
                            Iterator itor = keySet.iterator();
                            while (itor.hasNext()) {
                                String tempname = itor.next().toString();
                                if (tempname.equals(headlist.get(i))) {
                                    boo = true;
                                }
                            }
                            if (boo == false) {
                                throw new Exception("表头字段和定义的属性字段不匹配，请检查");
                            }
                        }

                        Iterator itor = keySet.iterator();
                        while (itor.hasNext()) {
                            boolean boo = false;
                            String tempname = itor.next().toString();
                            for (int i = 0; i < headlist.size(); i++) {
                                if (tempname.equals(headlist.get(i))) {
                                    boo = true;
                                }
                            }
                            if (boo == false) {
                                throw new Exception("表头字段和定义的属性字段不匹配，请检查");
                            }
                        }
                        // 读取到列后，检查表头是否完全一致--end
                    }

                } else {
                    Object obj = excelParam.getClazz().newInstance();
                    Iterator it = keySet.iterator();
                    while (it.hasNext()) {
                        Object key = it.next();
                        Integer cellNum_x = cellmap.get(excelParam.getMap().get(key).toString());
                        if (cellNum_x == null || hssfRow.getCell(cellNum_x) == null) {
                            continue;
                        }
                        String attr = excelParam.getMap().get(key).toString();// 得到属性

                        Class<?> attrType = BeanUtils.findPropertyType(attr, new Class[] { obj.getClass() });

                        Cell cell = hssfRow.getCell(cellNum_x);
                        getValue(cell, obj, attr, attrType, rowNum, cellNum_x, key);

                    }
                    list.add(obj);
                }

            }
        }
        is.close();
        return list;
    }

    public static void commonExportExcel(ExcelParam excelParam) throws Exception {

        Map<String, String> map = getMap(excelParam.getClazz());
        List<String> keyList = null;
        if(StringUtils.isEmpty(excelParam.getKeyValue())){
            keyList = getList(excelParam.getClazz());
        }else{
            keyList = getList(excelParam.getKeyValue());
        }

        Object obj = excelParam.getClazz().newInstance();
        // 创建HSSFWorkbook对象(excel的文档对象)
        Workbook wb = new XSSFWorkbook();
        // 建立新的sheet对象（excel的表单）
        Sheet sheet = wb.createSheet("sheet1");
        // 声明样式
        CellStyle style = wb.createCellStyle();
        // 居中显示
        style.setAlignment(HorizontalAlignment.CENTER);
        // 在sheet里创建第一行为表头，参数为行索引(excel的行)
        Row rowHeader = sheet.createRow(0);
        // 创建单元格并设置单元格内容

        // 存储属性信息
        Map<String, String> attMap = new HashMap();
        int index = 0;

        for (String key : keyList) {
            rowHeader.createCell(index).setCellValue(key);
            attMap.put(Integer.toString(index), map.get(key).toString());
            index++;
        }

        // 在sheet里创建表头下的数据
        for (int i = 0; i < excelParam.getList().size(); i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < map.size(); j++) {
                Class<?> attrType = BeanUtils.findPropertyType(attMap.get(Integer.toString(j)),
                        new Class[] { obj.getClass() });
                Object value = getAttrVal(excelParam.getList().get(i), attMap.get(Integer.toString(j)), attrType);
                if (null == value) {
                    value = "";
                }
                row.createCell(j).setCellValue(value.toString());
                style.setAlignment(HorizontalAlignment.CENTER);
            }
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String newFileName = excelParam.getFileName();
        if (StringUtils.isEmpty(newFileName)) {
            newFileName = df.format(new Date());
        }

        // 输出Excel文件
        try {
            if(excelParam.getResponse() != null) {
                OutputStream outstream = excelParam.getResponse().getOutputStream();
                excelParam.getResponse().reset();
                excelParam.getResponse().setHeader("Content-disposition",
                        "attachment; filename=" + new String(newFileName.getBytes(), "iso-8859-1") + ".xlsx");
                excelParam.getResponse().setContentType("application/x-download");
                wb.write(outstream);
                outstream.close();
            }else {
                FileOutputStream out = new FileOutputStream(excelParam.getOutFilePath());
                wb.write(out);
                out.close();
            }

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("导出失败！" + e);
        } catch (IOException e) {
            throw new IOException("导出失败！" + e);
        }

    }


    public static void setter(Object obj, String att, Object value, Class<?> type, int row, int col, Object key)
            throws Exception {
        try {
            Method method = obj.getClass().getMethod("set" + StringUtil.toUpperCaseFirstOne(att), type);
            method.invoke(obj, value);
        } catch (Exception e) {
            throw new Exception("第" + (row + 1) + " 行  " + (col + 1) + "列   属性：" + key + " 赋值异常  " + e);
        }
    }


    public static Object getAttrVal(Object obj, String att, Class<?> type) throws Exception {
        try {
            Method method = obj.getClass().getMethod("get" + StringUtil.toUpperCaseFirstOne(att));
            Object value = method.invoke(obj);
            return value;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * @Function 得到Excel列的值
     * @author   likaixuan
     * @Date     2019-07-05 15:07
     * @param cell
     * @param obj
     * @param attr
     * @param attrType
     * @param row
     * @param col
     * @param key
     * @return   void
     */
    public static void getValue(Cell cell, Object obj, String attr, Class attrType, int row, int col, Object key)
            throws Exception {
        Object val = null;

        if (cell.getCellType() == CellType.BOOLEAN) {
            val = cell.getBooleanCellValue();

        } else if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    if (attrType == String.class) {
                        val = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                    } else {
                        val = dateConvertFormat(sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue())));
                    }
                } catch (ParseException e) {
                    throw new Exception("第" + (row + 1) + " 行  " + (col + 1) + "列   属性：" + key + " 日期格式转换错误  ");
                }
            } else {
                if (attrType == String.class) {
                    cell.setCellType(CellType.STRING);
                    val = cell.getStringCellValue();
                } else if (attrType == BigDecimal.class) {
                    val = new BigDecimal(cell.getNumericCellValue());
                } else if (attrType == long.class) {
                    val = (long) cell.getNumericCellValue();
                } else if (attrType == Double.class) {
                    val = cell.getNumericCellValue();
                } else if (attrType == Float.class) {
                    val = (float) cell.getNumericCellValue();
                } else if (attrType == int.class || attrType == Integer.class) {
                    val = (int) cell.getNumericCellValue();
                } else if (attrType == Short.class) {
                    val = (short) cell.getNumericCellValue();
                } else {
                    val = cell.getNumericCellValue();
                }
            }

        } else if (cell.getCellType() == CellType.STRING) {
            if(attrType.equals(double.class) || attrType.equals(Double.class)){
                val = Double.parseDouble(cell.getStringCellValue());
            }else{
                val = cell.getStringCellValue();
            }

        }

        setter(obj, attr, val, attrType, row, col, key);
    }

    /**
     * String类型日期转为Date类型
     *
     * @author   likaixuan
     * @Date     2019-07-05 16:45
     * @param    dateStr
     * @return   java.util.Date
     * @throws Exception
     */
    public static Date dateConvertFormat(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(dateStr);
        return date;
    }

}
