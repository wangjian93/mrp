package com.ivo;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.utils.ExcelUtil;
import com.ivo.mrp.service.VenderArrivalPlanService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wj
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback(false)
public class TestExcel {

    private static String[] propertyTypes = {"vender", "model", "vender_material", "material", "quantityType"};
    private static String[] quantityTypes = {"pp", "fcs_ive", "fcst_ivo", "act_ive", "act_ivo"};
    private static String path = "/Users/wangjian/Downloads/集齐七表测试2.xlsx";

    public static void main(String[] args) throws Exception {
        List<List<Object>> list = null;
        try {
            list = ExcelUtil.readExcelFirstSheet(path);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("excel读取失败");
            throw e;
        }
        if(list == null || list.size() == 0) {
            return;
        }

        int rowInt = 0;
        int colInt = 0;
        try {


            // 解析第一行
            List<Object> firstRow = list.get(0);
            // 日期数组
            Date[] dates = new Date[firstRow.size()];
            for(;colInt<firstRow.size(); colInt++) {
                if(colInt<propertyTypes.length) {
                    continue;
                }
                Object object = firstRow.get(colInt);
                if(object instanceof String) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
                    String dateStr = (String) object;
                    dateStr =  dateStr.trim().replace("/", "-");
                    dates[colInt] = sdf.parse(dateStr);
                } else {
                    dates[colInt] = (Date) object;
                }
            }
            colInt = 0;
            rowInt++;

            // 解析后面数据
            String[] propertyValues = new String[propertyTypes.length];
            Map<Date, Map> map = new HashMap<>();
            int l=0;
            for(;rowInt<list.size(); rowInt++) {
                List row = list.get(rowInt);
                // 前面列
                for(; colInt<propertyTypes.length; colInt++) {
                    String value = ((String) row.get(colInt)).trim();
                    if(StringUtils.isNoneEmpty(value)) {
                        propertyValues[colInt] = value;
                    }
                }
                // 后面列
                for(; colInt<row.size(); colInt++) {
                    Object value = row.get(colInt);
                    Double d;
                    if(value == null) {
                        continue;
                    }
                    if(value instanceof String) {
                        if(StringUtils.isEmpty((String)value)) {
                            continue;
                        }
                        d = Double.valueOf((String) value);
                    } else {
                        d = ((BigDecimal) value).doubleValue();
                    }

                    Map subMap = map.get(dates[colInt]);
                    if(subMap == null) {
                        subMap = new HashMap();
                        for(int i=0; i<propertyTypes.length; i++) {
                            subMap.put(propertyTypes[i], propertyValues[i]);
                        }
                        map.put(dates[colInt], subMap);
                    }
                    subMap.put(quantityTypes[l], d);
                }


                l++;
                if(l == quantityTypes.length) {
                    ObjectMapper mapper = new ObjectMapper();
                    String string = mapper.writeValueAsString(map);
                    System.out.println(string);
                    l=0;
                    map.clear();
                }

                colInt = 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("上传失败，Excel格式不准确，第"+rowInt+"行第"+colInt+"列");
        }

    }


    @Autowired

    private VenderArrivalPlanService venderArrivalPlanService;

    @Test
    public void test() throws Exception {
        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);

        venderArrivalPlanService.uploadVenderArrivalPlan(inputStream, file.getName());
    }
}
