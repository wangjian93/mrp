package com.ivo.rest.oracle.service.impl;

import com.ivo.rest.oracle.mapper.InventoryMapper;
import com.ivo.rest.oracle.service.OracleInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

/**
 * @author wj
 * @version 1.0
 */
@Service
@Slf4j
public class OracleInventoryServiceImpl implements OracleInventoryService {

    private InventoryMapper inventoryMapper;

    @Autowired
    public OracleInventoryServiceImpl(InventoryMapper inventoryMapper) {
        this.inventoryMapper = inventoryMapper;
    }

    @Override
    public double getGoodInventory(String plant, String material, Date fabDate) {
        Double d = null;
        if(StringUtils.equalsIgnoreCase(plant, "LCM1")) {
            d = inventoryMapper.getLcm1GoodInventory(material, fabDate.toString());
        }
        if(StringUtils.equalsIgnoreCase(plant, "LCM2")) {
            d = inventoryMapper.getLcm2GoodInventory(material, fabDate.toString());
        }
        if(StringUtils.equalsIgnoreCase(plant, "CELL")) {
            d = inventoryMapper.getCellGoodInventory(material, fabDate.toString());
        }
        return d == null ? 0 : d;
    }

    @Override
    public double getDullInventory(String plant, String material, Date fabDate) {
        Double d = null;
        if(StringUtils.equalsIgnoreCase(plant, "LCM1")) {
            d = inventoryMapper.getLcm1DullInventory(material, fabDate.toString());
        }
        if(StringUtils.equalsIgnoreCase(plant, "LCM2")) {
            d = inventoryMapper.getLcm2DullInventory(material, fabDate.toString());
        }
        if(StringUtils.equalsIgnoreCase(plant, "CELL")) {
            d = inventoryMapper.getCellDullInventory(material, fabDate.toString());
        }
        return d == null ? 0 : d;
    }
}
