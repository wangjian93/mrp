package com.ivo.mrp2.service.impl;

import com.ivo.common.utils.ExcelUtil;
import com.ivo.mrp2.entity.MaterialLossRate;
import com.ivo.mrp2.repository.MaterialLossRateRepository;
import com.ivo.mrp2.service.BomService;
import com.ivo.mrp2.service.MaterialLossRateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
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

    private BomService bomService;

    @Autowired
    public MaterialLossRateServiceImpl(MaterialLossRateRepository lossRateRepository,
                                       BomService bomService) {
        this.lossRateRepository = lossRateRepository;
        this.bomService = bomService;
    }

    @Override
    public Double getMaterialLossRate(String material) {
        // 当料号没有维护损耗率时将返回物料组的损耗率
        Date date = new Date(System.currentTimeMillis());
        MaterialLossRate lossRate = lossRateRepository.findFirstByMaterialAndEffectFlag(material, true);
        if(lossRate == null) {
            String materialGroup = bomService.getMaterialGroup(material);
            if(StringUtils.isNotEmpty(materialGroup)) {
                lossRate = lossRateRepository.findFirstByMaterialGroupAndEffectFlag(materialGroup, true);
            }
        }
        if(lossRate == null) return 0D;
        return lossRate.getLossRate() == null ? 0D : lossRate.getLossRate();
    }

    @Override
    public List<MaterialLossRate> getMaterialLossRate() {
        return lossRateRepository.findAll();
    }

    @Override
    public void saveMaterialLossRate(String material, double lossRate, String memo) {
        Date date = new Date(System.currentTimeMillis());
        // 失效旧记录
        MaterialLossRate oldMaterialLossRate = lossRateRepository.findFirstByMaterialAndEffectFlag(material, true);
        if(oldMaterialLossRate != null) {
            oldMaterialLossRate.setExpireDate(date);
            oldMaterialLossRate.setEffectFlag(false);
            lossRateRepository.save(oldMaterialLossRate);
        }
        // 保存新记录
        MaterialLossRate newMaterialLossRate = new MaterialLossRate();
        newMaterialLossRate.setMaterial(material);
        newMaterialLossRate.setEffectDate(date);
        newMaterialLossRate.setLossRate(lossRate);
        newMaterialLossRate.setMemo(memo);
        newMaterialLossRate.setMaterialGroup(bomService.getMaterialGroup(material));
        newMaterialLossRate.setMaterialName(bomService.getMaterialName(material));
        newMaterialLossRate.setEffectFlag(true);
        lossRateRepository.save(newMaterialLossRate);
    }

    @Override
    public void saveMaterialGroupLossRate(String materialGroup, double lossRate, String memo) {
        Date date = new Date(System.currentTimeMillis());
        MaterialLossRate oldMaterialLossRate = lossRateRepository.findFirstByMaterialGroupAndEffectFlag(materialGroup, true);
        if(oldMaterialLossRate != null) {
            oldMaterialLossRate.setExpireDate(date);
            oldMaterialLossRate.setEffectFlag(false);
            lossRateRepository.save(oldMaterialLossRate);
        }
        MaterialLossRate newMaterialLossRate = new MaterialLossRate();
        newMaterialLossRate.setMaterialGroup(materialGroup);
        newMaterialLossRate.setEffectDate(date);
        newMaterialLossRate.setLossRate(lossRate);
        newMaterialLossRate.setMemo(memo);
        newMaterialLossRate.setEffectFlag(true);
        lossRateRepository.save(newMaterialLossRate);
    }

    @Override
    public void abolishLossRate(long[] ids) {
        List<MaterialLossRate> materialLossRateList = new ArrayList<>();
        for(long id : ids) {
            MaterialLossRate materialLossRate = lossRateRepository.findById(id).orElse(null);
            if(materialLossRate == null) continue;
            Date date = new Date(System.currentTimeMillis());
            materialLossRate.setExpireDate(date);
            materialLossRate.setEffectFlag(false);
            materialLossRateList.add(materialLossRate);
        }
        lossRateRepository.saveAll(materialLossRateList);
    }

    @Override
    public Page<MaterialLossRate> getPageMaterialLossRate(int page, int limit, String material, String materialGroup, String effectFlag) {
        return null;
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
                saveMaterialLossRate(material, lossRate, memo);
                continue;
            }
            if(StringUtils.isNotEmpty(materialGroup)) {
                saveMaterialGroupLossRate(materialGroup, lossRate, memo);
            }
        }
    }
}
