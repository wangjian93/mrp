package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.MaterialLossRate;
import com.ivo.mrp2.repository.MaterialLossRateRepository;
import com.ivo.mrp2.service.BomService;
import com.ivo.mrp2.service.MaterialLossRateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
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
        MaterialLossRate lossRate = lossRateRepository.getEffectRate(material, date);
        if(lossRate == null) {
            String materialGroup = bomService.getMaterialGroup(material);
            if(StringUtils.isNotEmpty(materialGroup)) {
                lossRate = lossRateRepository.getEffectRateByMaterialGroup(materialGroup, date);
            }
        }
        if(lossRate == null) return 0D;
        return lossRate.getLossRate() == null ? 0D : lossRate.getLossRate();
    }

    @Override
    public List<MaterialLossRate> getMaterialLossRate() {
        return lossRateRepository.findAll();
    }
}
