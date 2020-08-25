package com.ivo.mrp2.service.impl;

import com.ivo.common.utils.ExcelUtil;
import com.ivo.mrp2.entity.MaterialLossRate;
import com.ivo.mrp2.repository.MaterialLossRateRepository;
import com.ivo.mrp2.service.MaterialGroupService;
import com.ivo.mrp2.service.MaterialLossRateService;
import com.ivo.mrp2.service.MaterialService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class MaterialLossRateServiceImpl implements MaterialLossRateService {

    private MaterialLossRateRepository lossRateRepository;

    private MaterialService materialService;
    private MaterialGroupService materialGroupService;

    @Autowired
    public MaterialLossRateServiceImpl(MaterialLossRateRepository lossRateRepository,
                                       MaterialService materialService,
                                       MaterialGroupService materialGroupService) {
        this.lossRateRepository = lossRateRepository;
        this.materialService = materialService;
        this.materialGroupService = materialGroupService;
    }

    @Override
    public void importLossRate(InputStream inputStream, String fileName) {
        List<List<Object>> list;
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

        for(int i=1; i<list.size(); i++) {
            List<Object> objectList = list.get(i);
            String materialGroup;
            if(objectList.get(0) instanceof BigDecimal) {
                materialGroup = objectList.get(0).toString();
            } else {
                materialGroup = (String) objectList.get(0);
            }

            String material;
            if(objectList.get(1) instanceof BigDecimal) {
                material = objectList.get(1).toString();
            } else {
                material = (String) objectList.get(1);
            }

            double lossRate = ((BigDecimal) objectList.get(2)).doubleValue();

            String  memo = null;
            if(objectList.size() > 3) {
                if(objectList.get(3) instanceof BigDecimal) {
                    memo = objectList.get(3).toString();
                } else {
                    memo = (String) objectList.get(3);
                }
            }


            if(StringUtils.isNotEmpty(material)) {
                saveForMaterial(material, lossRate);
                continue;
            }
            if(StringUtils.isNotEmpty(materialGroup)) {
                saveForMaterialGroup(materialGroup, lossRate);
            }
        }
    }


    @Override
    public double getMaterialLossRate(String material) {
        MaterialLossRate materialLossRate = getMaterialLossRateByMaterial(material);
        if(materialLossRate == null) {
            materialLossRate = getMaterialLossRateByMaterialGroup(materialService.getMaterialGroup(material));
        }
        if(materialLossRate == null) {
            return 0;
        } else {
            return materialLossRate.getLossRate();
        }
    }

    @Override
    public Page<MaterialLossRate> getMaterialLossRate(int page, int limit, String materialGroup, String material) {
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "materialGroup");
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "material");
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
        Sort sort = Sort.by(orderList);
        Pageable pageable = PageRequest.of(page, limit, sort);
        return lossRateRepository.findByMaterialGroupLikeAndMaterialLike(materialGroup+"%", material+"%", pageable);
    }

    @Override
    public MaterialLossRate getMaterialLossRateByMaterial(String material) {
        return lossRateRepository.findFirstByMaterialAndType(material, 1);
    }

    @Override
    public MaterialLossRate getMaterialLossRateByMaterialGroup(String materialGroup) {
        return lossRateRepository.findFirstByMaterialGroupAndType(materialGroup, 2);
    }

    @Override
    public void saveForMaterial(String material, double lossRate) {
        log.info("维护损耗率>> START");
        if(lossRate>100 || lossRate<0) throw new RuntimeException("损耗率范围不正确，应在0~100");
        MaterialLossRate old = getMaterialLossRateByMaterial(material);
        if(old != null) lossRateRepository.delete(old);

        MaterialLossRate materialLossRate = new MaterialLossRate();
        materialLossRate.setType(1);
        materialLossRate.setLossRate(lossRate);
        materialLossRate.setMaterial(material);
        materialLossRate.setMaterialName(materialService.getMaterialName(material));
        String materialGroup = materialService.getMaterialGroup(material);
        materialLossRate.setMaterialGroup(materialGroup);
        materialLossRate.setMaterialGroupName(materialGroupService.getMaterialGroupName(materialGroup));
        lossRateRepository.save(materialLossRate);
        log.info("维护损耗率>> END");
    }

    @Override
    public void saveForMaterialGroup(String materialGroup, double lossRate) {
        log.info("维护损耗率>> START");
        if(lossRate>100 || lossRate<0) throw new RuntimeException("损耗率范围不正确，应在0~100");
        MaterialLossRate old = getMaterialLossRateByMaterialGroup(materialGroup);
        if(old != null) lossRateRepository.delete(old);

        MaterialLossRate materialLossRate = new MaterialLossRate();
        materialLossRate.setType(2);
        materialLossRate.setLossRate(lossRate);
        materialLossRate.setMaterial("");
        materialLossRate.setMaterialName("");
        materialLossRate.setMaterialGroup(materialGroup);
        materialLossRate.setMaterialGroupName(materialGroupService.getMaterialGroupName(materialGroup));
        lossRateRepository.save(materialLossRate);
        log.info("维护损耗率>> END");
    }

    @Override
    public void delLossRate(long id) {
        lossRateRepository.deleteById(id);
    }
}
