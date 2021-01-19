package com.ivo.mrp.service.impl;

import com.ivo.common.utils.DateUtil;
import com.ivo.mrp.entity.Inventory;
import com.ivo.mrp.entity.LossRate;
import com.ivo.mrp.repository.InventoryRepository;
import com.ivo.mrp.service.InventoryService;
import com.ivo.mrp.service.PositionService;
import com.ivo.rest.RestService;
import com.ivo.rest.oracle.OracleMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Service
public class InventoryServiceImpl implements InventoryService {

    private InventoryRepository inventoryRepository;

    private PositionService positionService;

    private OracleMapper oracleMapper;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository, PositionService positionService, OracleMapper oracleMapper) {
        this.inventoryRepository = inventoryRepository;
        this.positionService = positionService;
        this.oracleMapper = oracleMapper;
    }

    @Override
    public Page<Inventory> getPageInventory(int page, int limit, Date fabDate, String material) {
        Pageable pageable = PageRequest.of(page, limit);
        return inventoryRepository.findByFabDateAndMATNRLike(fabDate, material+"%", pageable);
    }

    @Override
    public List<Map> getGoodInventory(String plant, List<String> materialList, Date fabDate) {
        if(materialList == null || materialList.size()==0) return new ArrayList<>();
        if(StringUtils.equalsIgnoreCase(plant, "LCM1")) {
            String fab = "3000";
            List<String> positionList = positionService.getPositionIveGood();
            return inventoryRepository.getInventoryBatch( materialList, fabDate, fab, positionList);
        } else {
            String fab = "1000";
            List<String> positionList = positionService.getPositionIvoGood();
            return inventoryRepository.getInventoryBatch(materialList, fabDate, fab, positionList);
        }
    }

    @Override
    public List<Map> getDullInventory(String plant, List<String> materialList, Date fabDate) {
        if(materialList == null || materialList.size()==0) return new ArrayList<>();
        if(StringUtils.equalsIgnoreCase(plant, "LCM1")) {
            String fab = "3000";
            List<String> positionList = positionService.getPositionIveDull();
            return inventoryRepository.getInventoryBatch( materialList, fabDate, fab, positionList);
        } else {
            String fab = "1000";
            List<String> positionList = positionService.getPositionIvoDull();
            return inventoryRepository.getInventoryBatch(materialList, fabDate, fab, positionList);
        }
    }

    @Override
    public void syncInventory() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fabDate = sdf.format(DateUtil.getFirstDayOfMonth(new java.util.Date()));
        List<String> positionList = positionService.getPositionAll();
        List<Map> mapList = oracleMapper.getInventoryAll(fabDate, positionList);
        List<Inventory> inventoryList = new ArrayList<>();
        for(Map map : mapList) {
            Inventory inventory = new Inventory();
            inventory.setFabDate(new Date(((java.util.Date) map.get("FAB_DATE")).getTime()) );
            inventory.setMATNR((String) map.get("MATNR"));
            inventory.setMATKL((String) map.get("MATKL"));
            inventory.setLGORT((String) map.get("LGORT"));
            inventory.setWERKS((String) map.get("WERKS"));
            inventory.setMEINS((String) map.get("MEINS"));
            inventory.setLABST(((BigDecimal)map.get("LABST")).doubleValue());
            inventoryList.add(inventory);
        }
        inventoryRepository.saveAll(inventoryList);
    }

    @Override
    public Workbook exportExcel(Date fabDate) {
        List<Inventory> inventoryList = inventoryRepository.findByFabDate(fabDate);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        String[] titleItems = new String[] {"日期","料号", "MATKL", "工厂","仓位", " MEINS", "数量"};

        int intRow =0;
        int intCel = 0;
        Row row1 = sheet.createRow(intRow);
        for(; intCel<titleItems.length; intCel++) {
            Cell cell = row1.createCell(intCel);
            cell.setCellValue(titleItems[intCel]);
        }

        for(Inventory inventory : inventoryList) {
            intRow++;
            intCel = 0;
            Row row = sheet.createRow(intRow);
            row.createCell(intCel++).setCellValue(inventory.getFabDate().toString());
            row.createCell(intCel++).setCellValue(inventory.getMATNR());
            row.createCell(intCel++).setCellValue(inventory.getMATKL());
            row.createCell(intCel++).setCellValue(inventory.getWERKS());
            row.createCell(intCel++).setCellValue(inventory.getLGORT());
            row.createCell(intCel++).setCellValue(inventory.getMEINS());
            row.createCell(intCel++).setCellValue(inventory.getLABST());
        }
        return workbook;
    }
}
