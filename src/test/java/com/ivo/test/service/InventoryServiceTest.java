package com.ivo.test.service;

import com.ivo.mrp.entity.Inventory;
import com.ivo.mrp.service.InventoryService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wj
 * @version 1.0
 */
public class InventoryServiceTest extends AbstractTest {

    @Autowired
    private InventoryService inventoryService;

    @Test
    public void test() {
        inventoryService.syncInventory();
    }
}
