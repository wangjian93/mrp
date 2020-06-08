package com.ivo.rest.oracle.service.impl;

import com.ivo.rest.oracle.mapper.InventoryMapper;
import com.ivo.rest.oracle.service.OracleInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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



    // 良品仓位
    private static final String[] position_lcm1 = {"3010","3150", "4010"};
    private static final String[] position_lcm2 = {"2100","2101", "5610", "5611", "1400", "1401"};
    private static final String[] position_cell = {"1100", "1111", "1200", "5300", "1400", "1401"};
    // 呆滞品仓位
    private static final String[] positionDull_lcm1 = {"3060"};
    private static final String[] positionDull_lcm2 = {"1310","1311"};
    private static final String[] positionDull_cell = {"1310","1311"};
    // 厂别
    private static final String plant_lcm1 = "3000";
    private static final String plant_lcm2 = "1000";
    private static final String plant_cell = "1000";
    @Override
    public List<Map> getGoodInventory(List<String> materialList, Date fabDate, String plant) {
        if(StringUtils.equalsIgnoreCase(plant, "LCM1")) {
            return inventoryMapper.getInventory(materialList, fabDate.toString(), plant_lcm1, Arrays.asList(position_lcm1));
        }
        if(StringUtils.equalsIgnoreCase(plant, "LCM2")) {
            return inventoryMapper.getInventory(materialList, fabDate.toString(), plant_lcm2, Arrays.asList(position_lcm2));
        }
        if(StringUtils.equalsIgnoreCase(plant, "CELL")) {
            return inventoryMapper.getInventory(materialList, fabDate.toString(), plant_cell, Arrays.asList(position_cell));
        }
        return new ArrayList<>();
    }

    @Override
    public List<Map> getDullInventory(List<String> materialList, Date fabDate, String plant) {
        if(StringUtils.equalsIgnoreCase(plant, "LCM1")) {
            return inventoryMapper.getInventory(materialList, fabDate.toString(), plant_lcm1, Arrays.asList(positionDull_lcm1));
        }
        if(StringUtils.equalsIgnoreCase(plant, "LCM2")) {
            return inventoryMapper.getInventory(materialList, fabDate.toString(), plant_lcm2, Arrays.asList(positionDull_lcm2));
        }
        if(StringUtils.equalsIgnoreCase(plant, "CELL")) {
            return inventoryMapper.getInventory(materialList, fabDate.toString(), plant_cell, Arrays.asList(positionDull_cell));
        }
        return new ArrayList<>();
    }
}
