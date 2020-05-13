package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.MaterialDailyBalance;
import com.ivo.mrp2.repository.MaterialDailyBalanceRepository;
import com.ivo.mrp2.service.MaterialDailyBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class MaterialDailyBalanceServiceImpl implements MaterialDailyBalanceService {

    private MaterialDailyBalanceRepository balanceRepository;

    @Autowired
    public MaterialDailyBalanceServiceImpl(MaterialDailyBalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    @Override
    public void batchSave(List<MaterialDailyBalance> list) {
        balanceRepository.saveAll(list);
    }

    @Override
    public List<MaterialDailyBalance> getMaterialDailyBalance(String mrpVer) {
        return balanceRepository.findByMrpVer(mrpVer);
    }

    @Override
    public List<MaterialDailyBalance> getMaterialDailyBalance(String mrpVer, String material) {
        return balanceRepository.findByMrpVerAndMaterial(mrpVer, material);
    }
}
