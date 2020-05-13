package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {
    @Override
    public Double getBeginInventory(String material, Date startDate) {
        return 1000D;
    }
}
