package com.ivo.mrp.service.impl;

import com.ivo.common.utils.ExcelUtil;
import com.ivo.mrp.entity.pol.BomPol;
import com.ivo.mrp.repository.BomPolRepository;
import com.ivo.mrp.service.BomPolService;
import com.ivo.mrp.service.MaterialService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Service
@Slf4j
public class BomPolServiceImpl implements BomPolService {

    private BomPolRepository bomPolRepository;
    private MaterialService materialService;

    @Autowired
    public BomPolServiceImpl(BomPolRepository bomPolRepository, MaterialService materialService) {
        this.bomPolRepository = bomPolRepository;
        this.materialService = materialService;
    }

    @Override
    public Page<BomPol> queryBomPol(int page, int limit, String searchProduct) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "product", "sort", "type");
        return bomPolRepository.findByProductLike(searchProduct+"%", pageable);
    }

    @Override
    public List<BomPol> queryBomPol(String searchProduct) {
        Sort sort = new Sort(Sort.Direction.ASC, "product", "sort", "type");
        return bomPolRepository.findByProductLike(searchProduct+"%", sort);
    }

    @Override
    public void importBomPol(InputStream inputStream, String fileName) {
        log.info("POL BOM数据导入 >> START");
        String[] titleNames = new String[] {"product", "material", "物料组", "物料名", "type", "sort"};
        List<List<Object>> list = ExcelUtil.readExcelFirstSheet(inputStream, fileName);
        if(list == null || list.size() < 1) return;
        int rowInt = 1;
        int cellInt = 0;
        Map<String, List<Map>> productMap = new HashMap<>();
        try{
            String key = null;
            for(; rowInt<list.size(); rowInt++) {
                List<Object> row = list.get(rowInt);
                Map<String, Object> map = new HashMap<>();
                cellInt = 0;
                for(; cellInt<row.size(); cellInt++) {
                    if(cellInt >= titleNames.length) break;
                    map.put(titleNames[cellInt], row.get(cellInt));
                }
                String product = (String) map.get("product");
                //处理单元格合并情况
                if(StringUtils.isEmpty(product)) {
                    product = key;
                } else {
                    key =  product;
                }
                if(product == null) continue;

                List<Map> productList = productMap.computeIfAbsent(product, k -> new ArrayList<>());
                productList.add(map);
            }
        } catch (Exception e) {
            throw new RuntimeException("EXCEL导入错误，第"+rowInt+"行，第"+cellInt+"列数据不准确."+e.getMessage());
        }

        for(String product : productMap.keySet()) {
            saveBomPol(product, productMap.get(product));
        }
        log.info("POL BOM数据导入 >> END");
    }

    @Override
    public Workbook exportBomPol(String searchProduct) {
        List<BomPol> bomPolList = queryBomPol(searchProduct);
        if(bomPolList == null) bomPolList = new ArrayList<>();
        return writeToExcel(bomPolList);
    }

    @Override
    public void saveBomPol(String product, List<Map> list) {
        bomPolRepository.deleteAll(getBomPol(product));
        List<BomPol> bomPolList = new ArrayList<>();
        for(Map map : list) {
            String material = (String) map.get("material");
            String type = (String) map.get("type");
            String sort = (String) map.get("sort");
            BomPol bomPol = new BomPol();
            bomPol.setFab("CELL");
            bomPol.setProduct(product);
            bomPol.setMaterial(material);
            bomPol.setType(type);
            bomPol.setSort(sort);
            bomPol.setMaterialGroup(materialService.getMaterialGroup(material));
            bomPol.setMaterialName(materialService.getMaterialName(material));
            bomPolList.add(bomPol);
        }
        bomPolRepository.saveAll(bomPolList);
    }

    @Override
    public List<BomPol> getBomPol(String product) {
        return bomPolRepository.findByProduct(product);
    }

    private Workbook writeToExcel(List<BomPol> bomPolList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        String[] titleItems = new String[] {"机种", "料号", "物料组", "物料名", "类型", "组"};

        //首行标题居中、加背景
        CellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.CENTER); //水平居中
        cellStyle1.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex()); //设置背景色
        cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND); //设置加粗
        //设置列宽
        sheet.setColumnWidth(0, 15*256);
        sheet.setColumnWidth(1, 20*256);
        sheet.setColumnWidth(2, 20*256);
        sheet.setColumnWidth(3, 40*256);
        sheet.setColumnWidth(4, 15*256);
        sheet.setColumnWidth(5, 15*256);
        //单元格居中
        CellStyle cellStyle2 = workbook.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.CENTER); //水平居中
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER); //垂直居中

        int intRow =0;
        int intCel = 0;
        Row row1 = sheet.createRow(intRow);
        for(; intCel<titleItems.length; intCel++) {
            Cell cell = row1.createCell(intCel);
            cell.setCellValue(titleItems[intCel]);
            cell.setCellStyle(cellStyle1);
        }

        for(BomPol bomPol : bomPolList) {
            intRow++;
            intCel = 0;
            Row row = sheet.createRow(intRow);
            Cell cell = row.createCell(intCel++);
            cell.setCellValue(bomPol.getProduct());
            cell.setCellStyle(cellStyle2);
            row.createCell(intCel++).setCellValue(bomPol.getMaterial());
            row.createCell(intCel++).setCellValue(bomPol.getMaterialGroup());
            row.createCell(intCel++).setCellValue(bomPol.getMaterialName());
            row.createCell(intCel).setCellValue(bomPol.getType());
            row.createCell(intCel).setCellValue(bomPol.getSort());
        }

        return workbook;
    }

    @Override
    public Workbook downloadBomPolExcel() {
        return writeToExcel(new ArrayList<>());
    }
}
