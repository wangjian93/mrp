package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.service.InventoryService;
import com.ivo.rest.oracle.service.OracleInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {

    private OracleInventoryService oracleInventoryService;

    @Autowired
    public InventoryServiceImpl(OracleInventoryService oracleInventoryService) {
        this.oracleInventoryService = oracleInventoryService;
    }

    @Override
    public double getGoodInventory(String plant, String material, Date fabDate) {
        return oracleInventoryService.getGoodInventory(plant, material, fabDate);
    }

    @Override
    public double getDullInventory(String plant, String material, Date fabDate) {
        return oracleInventoryService.getDullInventory(plant, material, fabDate);
    }
}
