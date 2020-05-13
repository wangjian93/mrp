//package com.ivo.mrp.service.impl;
//
//import com.ivo.common.utils.ExcelUtil;
//import com.ivo.mrp.entity.AttritionRate;
//import com.ivo.mrp.repository.AttritionRateRepository;
//import com.ivo.mrp.service.AttritionRateService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.math.BigDecimal;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * @author wj
// * @version 1.0
// */
//@Service
//@Slf4j
//public class AttritionRateServiceImpl implements AttritionRateService {
//
//    private AttritionRateRepository repository;
//
//    @Autowired
//    public AttritionRateServiceImpl(AttritionRateRepository repository) {
//        this.repository = repository;
//    }
//
//    @Override
//    public void importAttritionRate(InputStream excel, String fileName) throws Exception {
//        List<List<Object>> list;
//        if (fileName.endsWith("xls")) {
//            list =  ExcelUtil.readXlsFirstSheet(excel);
//        }
//        else if (fileName.endsWith("xlsx")) {
//            list =  ExcelUtil.readXlsxFirstSheet(excel);
//        }
//        else {
//            throw new IOException("文件类型错误");
//        }
//
//        int rowInt = 1;
//        int colInt = 0;
//
//        String[] cols = {"vender", "venderCode", "venderModel", "venderMaterial", "materialGroup", "material",
//                "attritionRate", "effectDate", "memo"};
//        for(; rowInt<list.size(); rowInt++) {
//            List<Object> row = list.get(rowInt);
//            HashMap<String, Object> map = new HashMap<>();
//            for(; colInt<cols.length; colInt++) {
//                map.put(cols[colInt], row.get(colInt));
//            }
//
//            AttritionRate attritionRate = new AttritionRate();
//            attritionRate.setVender((String) map.get("vender"));
//            attritionRate.setVenderCode((String) map.get("venderCode"));
//            attritionRate.setVenderModel((String) map.get("venderModel"));
//            attritionRate.setVenderMaterial((String) map.get("venderMaterial"));
//            attritionRate.setMaterialGroup((String) map.get("materialGroup"));
//            attritionRate.setMaterial((String) map.get("material"));
//            if(map.get("attritionRate") instanceof String) {
//                attritionRate.setAttritionRate( Double.valueOf( (String) map.get("attritionRate")) );
//            } else {
//                attritionRate.setAttritionRate( ((BigDecimal) map.get("attritionRate")).doubleValue() );
//            }
//
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
//            if(map.get("effectDate") instanceof String) {
//                String dateStr = (String) map.get("effectDate");
//                dateStr =  dateStr.trim().replace("/", "-");
//                attritionRate.setEffectDate(sdf.parse(dateStr));
//            } else {
//                attritionRate.setEffectDate((Date) map.get("effectDate"));
//            }
//            attritionRate.setMemo((String) map.get("memo"));
//            saveAttritionRate(attritionRate);
//            colInt = 0;
//        }
//    }
//
//    @Override
//    public void saveAttritionRate(AttritionRate attritionRate) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
//        // 对正生效的损耗率做失效操作
//        AttritionRate old = getEffectAttritionRate(attritionRate.getVenderCode(), attritionRate.getMaterial());
//        if(old != null) {
//            // 新增数据的生效日期不能不能比当前日期靠前
//            if(attritionRate.getEffectDate().before(new Date())) {
//                String msg = "损耗率保存失败，生效日期不能比今天日期靠前";
//                throw new RuntimeException(msg);
//            }
//
//            // 新增数据的生效日期不能比当前的生效日期靠前
//            if(attritionRate.getEffectDate().before(old.getEffectDate())) {
//                String msg = "损耗率保存失败，生效日期不能比当前的生效日期靠前";
//                throw new RuntimeException(msg);
//            }
//
//            old.setExpireDate(attritionRate.getEffectDate());
//            repository.save(old);
//        }
//
//        // 对已存在还未生效的数据进行删除操作
//        repository.deleteAll(getNoEffectAttritionRate(attritionRate.getVenderCode(), attritionRate.getMaterial()));
//
//        try {
//            attritionRate.setExpireDate(sdf.parse("9999-12-31"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        repository.save(attritionRate);
//    }
//
//    @Override
//    public void delAttritionRate(long id) {
//        AttritionRate attritionRate = repository.getOne(id);
//        if(attritionRate == null) {
//            return;
//        }
//
//        // 如果删除的是还未生效的数据，先修改当前已生效的失效时间为永久
//        AttritionRate effect = getEffectAttritionRate(attritionRate.getVenderCode(), attritionRate.getMaterial());
//        if(effect != null) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
//            try {
//                effect.setExpireDate(sdf.parse("9999-12-31"));
//                repository.save(effect);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        repository.delete(attritionRate);
//    }
//
//    /**
//     * 获取已存在还未生效的损耗率
//     * @param venderCode 供应商Code
//     * @param material 料号
//     * @return List
//     */
//    private List<AttritionRate> getNoEffectAttritionRate(String venderCode, String material) {
//        return repository.getNoEffectAttritionRate(venderCode, material, new Date());
//    }
//
//    @Override
//    public AttritionRate getEffectAttritionRate(String venderCode, String material) {
//        List<AttritionRate> list = repository.getEffectAttritionRate(venderCode, material, new Date());
//        if(list == null || list.size() == 0) {
//            return null;
//        }
//        return list.get(0);
//    }
//
//    @Override
//    public List<AttritionRate> getAttritionRate(String venderCode, String vender, String materialGroup, String material, boolean effectFlag) {
//        if(venderCode == null) venderCode = "";
//        if(vender == null) vender = "";
//        if(materialGroup == null) materialGroup = "";
//        if(material == null) material = "";
//
//        if(effectFlag) {
//            return repository.getEffectAttritionRate(venderCode, vender, materialGroup, material, new Date());
//        } else {
//            return repository.getAttritionRate(venderCode, vender, materialGroup, material);
//        }
//    }
//}
