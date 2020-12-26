package com.ivo.mrp.service;

import com.ivo.mrp.entity.Inventory;
import org.springframework.data.domain.Page;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface InventoryService {

    Page<Inventory> getPageInventory(int page, int limit, Date fabDate, String material);

    List<Map> getGoodInventory(String plant, List<String> materialList, Date fabDate);

    List<Map> getDullInventory(String plant, List<String> materialList, Date fabDate);

    void syncInventory();
}
