package com.ivo.mrp.service.impl;

import com.ivo.common.utils.ExcelUtil;
import com.ivo.mrp.entity.LossRate;
import com.ivo.mrp.entity.Supplier;
import com.ivo.mrp.entity.direct.SupplierMaterial;
import com.ivo.mrp.entity.packaging.SupplierPackage;
import com.ivo.mrp.repository.SupplierMaterialRepository;
import com.ivo.mrp.repository.SupplierPackageRepository;
import com.ivo.mrp.repository.SupplierRepository;
import com.ivo.mrp.service.SupplierService;
import com.ivo.rest.RestService;
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
import java.math.BigDecimal;
import java.util.*;

/**
 * 供应商与材料的服务接口
 * @author wj
 * @version 1.0
 */
@Service
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private SupplierRepository supplierRepository;

    private SupplierMaterialRepository supplierMaterialRepository;

    private SupplierPackageRepository supplierPackageRepository;

    private RestService restService;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository,
                               SupplierMaterialRepository supplierMaterialRepository,
                               SupplierPackageRepository supplierPackageRepository,
                               RestService restService) {
        this.supplierRepository = supplierRepository;
        this.supplierMaterialRepository = supplierMaterialRepository;
        this.supplierPackageRepository = supplierPackageRepository;
        this.restService = restService;
    }

    @Override
    public Supplier getSupplier(String supplierCode) {
        return supplierRepository.findById(supplierCode).orElse(null);
    }

    @Override
    public SupplierMaterial getSupplierMaterial(String supplierCode, String material) {
        return supplierMaterialRepository.findBySupplierCodeAndMaterialAndValidFlag(supplierCode, material, true);
    }

    @Override
    public List<Supplier> getSupplierByMaterial(String material) {
        List<SupplierMaterial> supplierMaterialList = supplierMaterialRepository.findByMaterialAndValidFlag(material, true);
        List<Supplier> supplierList = new ArrayList<>();
        for(SupplierMaterial supplierMaterial : supplierMaterialList) {
            Supplier supplier = supplierRepository.findById(supplierMaterial.getSupplierCode()).orElse(null);
            if(supplier != null) {
                supplierList.add(supplier);
            }
        }
        return supplierList;
    }

    @Override
    public void addSupplierMaterial(String supplierCode, String material, String user) {
        SupplierMaterial supplierMaterial = getSupplierMaterial(supplierCode, material);
        if(supplierMaterial != null) return;
        supplierMaterial = new SupplierMaterial();
        supplierMaterial.setSupplierCode(supplierCode);
        supplierMaterial.setMaterial(material);
        supplierMaterial.setCreator(user);
        supplierMaterial.setUpdater(user);
        supplierMaterialRepository.save(supplierMaterial);
    }

    @Override
    public void delSupplierMaterial(String supplierCode, String material, String user) {
        SupplierMaterial supplierMaterial = getSupplierMaterial(supplierCode, material);
        if(supplierMaterial == null) return;
        supplierMaterial.setValidFlag(false);
        supplierMaterial.setCreator(user);
        supplierMaterial.setUpdater(user);
        supplierMaterialRepository.save(supplierMaterial);
    }

    @Override
    public void importSupplierMaterial(InputStream inputStream, String fileName) {
        log.info("供应商数据导入 >> START");
        String[] titleNames = new String[] {"料号", "供应商Code", "供应商"};
        List<List<Object>> list = ExcelUtil.readExcelFirstSheet(inputStream, fileName);
        if(list == null || list.size() < 1) return;
        int rowInt = 1;
        int cellInt = 0;
        try {
            for(; rowInt<list.size(); rowInt++) {
                Map<String, String> map = new HashMap<>();
                List<Object> row = list.get(rowInt);
                for(; cellInt<row.size(); cellInt++) {
                    if(cellInt >= titleNames.length) break;
                    map.put(titleNames[cellInt], (String) row.get(cellInt));
                }
                String material =  map.get("料号").trim();
                String supplierCode = map.get("供应商Code").trim();
                if(!StringUtils.startsWith(supplierCode, "0000")) {
                    supplierCode = "0000"+supplierCode;
                }
                String supplierName = map.get("供应商").trim();
                Supplier supplier = getSupplier(supplierCode);
                if(supplier == null) {
                    String supplierSname = "";
                    if(supplierName.length()>6) supplierSname = supplierName.substring(0, 6);
                    addSupplier(supplierCode, supplierName, supplierSname, "SYS");
                }
                addSupplierMaterial(supplierCode, material, "SYS");
                cellInt = 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("EXCEL导入错误，第"+rowInt+"行，第"+cellInt+"列数据不准确."+e.getMessage());
        }
        log.info("供应商数据导入 >> END");
    }

    @Override
    public Workbook exportSupplierMaterial(String material, String supplier) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        String[] titleItems = new String[] {"料号","物料组", "物料名", "供应商ID", "供应商", "简称"};


        //首行标题居中、加背景
        CellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.CENTER); //水平居中
        cellStyle1.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex()); //设置背景色
        cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND); //设置加粗
        //设置列宽
        sheet.setColumnWidth(0, 20*256);
        sheet.setColumnWidth(1, 50*256);
        sheet.setColumnWidth(2, 20*256);

        int intRow =0;
        int intCel = 0;
        Row row1 = sheet.createRow(intRow);
        for(; intCel<titleItems.length; intCel++) {
            Cell cell = row1.createCell(intCel);
            cell.setCellValue(titleItems[intCel]);
            cell.setCellStyle(cellStyle1);
        }

        Page<Map> p = querySupplierMaterial(0, 100000, material, supplier);
        List<Map> mapList = p.getContent();
        for(Map map : mapList) {
            intRow++;
            intCel = 0;
            Row row = sheet.createRow(intRow);
            row.createCell(intCel++).setCellValue((String) map.get("material"));
            row.createCell(intCel++).setCellValue((String) map.get("materialGroup"));
            row.createCell(intCel++).setCellValue((String) map.get("materialName"));
            row.createCell(intCel++).setCellValue((String) map.get("supplierCode"));
            row.createCell(intCel++).setCellValue((String) map.get("supplierName"));
            row.createCell(intCel).setCellValue((String) map.get("supplierSname"));
        }
        return workbook;
    }

    @Override
    public void updateSupplier(String supplierCode, String supplierName, String supplierSname, String user) {
        Supplier supplier = getSupplier(supplierCode);
        if(supplier == null) throw new RuntimeException("供应商"+ supplierCode + "不存在");
        supplier.setSupplierName(supplierName);
        supplier.setSupplierSname(supplierSname);
        supplier.setUpdater(user);
        supplier.setUpdateDate(new Date());
        supplierRepository.save(supplier);
    }

    @Override
    public void addSupplier(String supplierCode, String supplierName, String supplierSname, String user) {
        Supplier supplier = getSupplier(supplierCode);
        if(supplier == null) {
            supplier = new Supplier();
            supplier.setCreator(user);
            supplier.setCreateDate(new Date());
        } else {
            supplier.setUpdater(user);
            supplier.setUpdateDate(new Date());
        }
        supplier.setSupplierCode(supplierCode);
        supplier.setSupplierName(supplierName);
        supplier.setSupplierSname(supplierSname);
        supplierRepository.save(supplier);
    }

    @Override
    public Page<Supplier> querySupplier(int page, int limit, String search) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "supplierCode");
        return supplierRepository.findBySupplierCodeLikeOrSupplierNameLikeOrSupplierSnameLike(search+"%", search+"%", search+"%", pageable);
    }

    @Override
    public Workbook exportExcel() {
        List<Supplier> supplierList = supplierRepository.findAll();
        return writeToExcel(supplierList);
    }

    public void importExcel(InputStream inputStream, String fileName) {
        String[] titleItems = new String[] {"供应商Code", "供应商名称", "简称"};
        List<List<Object>> list = ExcelUtil.readExcelFirstSheet(inputStream, fileName);
        int intRow =1;
        try {
            for(; intRow<list.size(); intRow++) {
                List<Object> dataList = list.get(intRow);
                Map<String, Object> map = new HashMap<>();
                for(int i=0; i<titleItems.length; i++) {
                    if(i==dataList.size()) break;
                    map.put(titleItems[i], dataList.get(i));
                }
                String code = ((String) map.get("供应商Code")).trim();
                String name = ((String) map.get("供应商名称")).trim();
                String sname = ((String) map.get("简称")).trim();
                addSupplier(code, name, sname, "SYS");
            }
        } catch (Exception e) {
            throw new RuntimeException("解析损耗率Excel第"+intRow+"行错误，数据异常", e);
        }
    }

    private Workbook writeToExcel(List<Supplier> supplierList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        String[] titleItems = new String[] {"供应商Code","供应商名称", "简称"};

        //首行标题居中、加背景
        CellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.CENTER); //水平居中
        cellStyle1.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex()); //设置背景色
        cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND); //设置加粗
        //设置列宽
        sheet.setColumnWidth(0, 20*256);
        sheet.setColumnWidth(1, 50*256);
        sheet.setColumnWidth(2, 20*256);

        int intRow =0;
        int intCel = 0;
        Row row1 = sheet.createRow(intRow);
        for(; intCel<titleItems.length; intCel++) {
            Cell cell = row1.createCell(intCel);
            cell.setCellValue(titleItems[intCel]);
            cell.setCellStyle(cellStyle1);
        }

        for(Supplier supplier : supplierList) {
            intRow++;
            intCel = 0;
            Row row = sheet.createRow(intRow);
            row.createCell(intCel++).setCellValue(supplier.getSupplierCode());
            row.createCell(intCel++).setCellValue(supplier.getSupplierName());
            row.createCell(intCel).setCellValue(supplier.getSupplierSname());
        }
        return workbook;
    }

    @Override
    public Page<Map> querySupplierMaterial(int page, int limit, String searchMaterial, String searchSupplier) {
        Pageable pageable = PageRequest.of(page, limit);
        return supplierMaterialRepository.querySupplierMaterial(searchMaterial+"%", searchSupplier+"%", pageable);
    }

    @Override
    public Page<SupplierPackage> querySupplierPackage(int page, int limit, String month, String searchProduct) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "project");
        return supplierPackageRepository.findByMonthAndProjectLikeAndValidFlag(month, searchProduct+"%", true, pageable);
    }

    @Override
    public void syncSupplierMaterial() {
        log.info("同步材料供应商>> START");
        List<Map> list = restService.getSupplierMaterial();
        if(list == null || list.size()==0) {
            log.warn("同步材料供应商>> END 无数据");
            return;
        }
        for(Map map : list) {
            String material = ((String) map.get("material")).trim();
            String supplierCode = ((String) map.get("supplierCode")).trim();
            if(!StringUtils.startsWith(supplierCode, "0000")) {
                supplierCode = "0000"+supplierCode;
            }
            String supplierName = ((String) map.get("supplierName")).trim();
            Supplier supplier = getSupplier(supplierCode);
            if(supplier == null) {
                String supplierSname;
                if(supplierName.length()>6) {
                    supplierSname = supplierName.substring(0, 6);
                } else {
                    supplierSname = supplierName;
                }
                addSupplier(supplierCode, supplierName, supplierSname, "SYS");
            }
            SupplierMaterial supplierMaterial = getSupplierMaterial(supplierCode, material);
            if(supplierMaterial == null) {
                addSupplierMaterial(supplierCode, material, "SYS");
            }
        }
        log.info("同步材料供应商>> END");
    }
}
