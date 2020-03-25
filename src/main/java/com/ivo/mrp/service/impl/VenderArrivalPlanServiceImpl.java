package com.ivo.mrp.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.utils.ExcelUtil;
import com.ivo.mrp.entity.VenderArrivalPlan;
import com.ivo.mrp.entity.VenderArrivalPlanDetail;
import com.ivo.mrp.repository.VenderArrivalPlanDetailRepository;
import com.ivo.mrp.repository.VenderArrivalPlanRepository;
import com.ivo.mrp.service.VenderArrivalPlanService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wj
 * @version 1.0
 */
@Service
public class VenderArrivalPlanServiceImpl implements VenderArrivalPlanService {

    private VenderArrivalPlanRepository repository;
    private VenderArrivalPlanDetailRepository detailRepository;

    @Autowired
    public VenderArrivalPlanServiceImpl(VenderArrivalPlanRepository repository,
                                        VenderArrivalPlanDetailRepository detailRepository) {
        this.repository = repository;
        this.detailRepository = detailRepository;
    }



    @Override
    public List<VenderArrivalPlan> getVenderArrivalPlan(String month, String vender, String material) {

        VenderArrivalPlan venderArrivalPlan = new VenderArrivalPlan();
        if(month != null && !month.equals("")) {
            venderArrivalPlan.setMonth(month);
        }
        if(vender != null && !vender.equals("")) {
            venderArrivalPlan.setVender(vender);
        }
        if(material != null && !material.equals("")) {
            venderArrivalPlan.setMaterial(material);
        }
        Example<VenderArrivalPlan> example = Example.of(venderArrivalPlan);
        return repository.findAll(example);
    }

    @Override
    public String uploadVenderArrivalPlan(InputStream inputStream, String fileName) throws Exception {
        String month;
        String[] propertyTypes = {"vender", "model", "vender_material", "material", "quantityType"};
        String[] quantityTypes = {"pp", "fcs_ive", "fcst_ivo", "act_ive", "act_ivo"};
        List<List<Object>> list = null;
        try {


            if (fileName.endsWith("xls")) {
                list =  ExcelUtil.readXlsFirstSheet(inputStream);
            }
            else if (fileName.endsWith("xlsx")) {
                list =  ExcelUtil.readXlsxFirstSheet(inputStream);
            }
            else {
                throw new IOException("文件类型错误");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("excel读取失败");
            throw new RuntimeException("excel读取失败," + e.getMessage(), e);
        }
        if(list == null || list.size() == 0) {
            throw new RuntimeException("excel无数据");
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

            // 清空当月数据
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            month = sdf.format(dates[propertyTypes.length]);
            repository.deleteAll(getVenderArrivalPlanByMonth(month));

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

                    Map<String, Double> subMap = map.get(dates[colInt]);
                    if(subMap == null) {
                        subMap = new HashMap();
//                        for(int i=0; i<propertyTypes.length; i++) {
//                            subMap.put(propertyTypes[i], propertyValues[i]);
//                        }
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

                    // 保存
                    Map<String, String> m = new HashMap<>();
                    for(int i=0; i<propertyTypes.length; i++) {
                        m.put(propertyTypes[i], propertyValues[i]);
                    }
                    VenderArrivalPlan venderArrivalPlan = new VenderArrivalPlan();
                    venderArrivalPlan.setVender(m.get("vender"));
                    venderArrivalPlan.setModel(m.get("model"));
                    venderArrivalPlan.setVender_material(m.get("vender_material"));
                    venderArrivalPlan.setMaterial(m.get("material"));
                    venderArrivalPlan.setMonth(month);

                    List<VenderArrivalPlanDetail> venderArrivalPlanDetailList = new ArrayList<>();
                    for (Date key : map.keySet()) {
                        Map<String, Double> subMap = map.get(key);
                        VenderArrivalPlanDetail venderArrivalPlanDetail = new VenderArrivalPlanDetail();
                        venderArrivalPlanDetail.setDate(key);
                        venderArrivalPlanDetail.setPp(subMap.get("pp"));
                        venderArrivalPlanDetail.setFcst_ive(subMap.get("fcs_ive"));
                        venderArrivalPlanDetail.setFcst_ivo(subMap.get("fcst_ivo"));
                        venderArrivalPlanDetail.setAct_ive(subMap.get("act_ive"));
                        venderArrivalPlanDetail.setAct_ivo(subMap.get("act_ivo"));

                        venderArrivalPlanDetail.setVenderArrivalPlan(venderArrivalPlan);
                        venderArrivalPlanDetailList.add(venderArrivalPlanDetail);
                    }
                    venderArrivalPlan.setVenderArrivalPlanDetailList(venderArrivalPlanDetailList);
                    repository.save(venderArrivalPlan);

                    map.clear();
                }

                colInt = 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("上传失败，Excel格式不准确，第"+rowInt+"行第"+colInt+"列");
            String msg = "上传失败，Excel格式不准确，第"+rowInt+"行第"+colInt+"列";
            throw new RuntimeException(msg, e);
        }
        return month;
    }

    @Override
    public List<VenderArrivalPlan> getVenderArrivalPlanByMonth(String month) {
        return repository.findByMonth(month);
    }
}
