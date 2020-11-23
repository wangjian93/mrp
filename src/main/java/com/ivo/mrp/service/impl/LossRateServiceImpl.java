package com.ivo.mrp.service.impl;

import com.ivo.common.utils.ExcelUtil;
import com.ivo.mrp.entity.LossRate;
import com.ivo.mrp.repository.LossRateRepository;
import com.ivo.mrp.service.LossRateService;
import com.ivo.mrp.service.MaterialGroupService;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class LossRateServiceImpl implements LossRateService {

    private LossRateRepository lossRateRepository;

    private MaterialService materialService;

    private MaterialGroupService materialGroupService;

    @Autowired
    public LossRateServiceImpl(LossRateRepository lossRateRepository, MaterialService materialService,
                               MaterialGroupService materialGroupService) {
        this.lossRateRepository = lossRateRepository;
        this.materialService = materialService;
        this.materialGroupService = materialGroupService;
    }

    private LossRate getLossRateByMaterial(String material) {
        return lossRateRepository.findFirstByMaterialAndTypeAndValidFlag(material, LossRate.type_material, true);
    }

    private LossRate getLossRateByMaterialGroup(String materialGroup) {
        return lossRateRepository.findFirstByMaterialGroupAndTypeAndValidFlag(materialGroup, LossRate.type_materialGroup, true);
    }

    private void deleteLossRate(LossRate lossRate, String user) {
        lossRate.setValidFlag(false);
        lossRate.setUpdater(user);
        lossRate.setUpdateDate(new Date());
        lossRateRepository.save(lossRate);
    }

    @Override
    public void saveMaterialLossRate(String material, double lossRate, String user, String materialGroup) {
        log.info("保存料号"+material+"的损耗率 >> START");
        //保存前先删除旧记录
        delMaterialLossRate(material, user);
        LossRate lr = new LossRate();
        lr.setType(LossRate.type_material);
        lr.setMaterial(material);
        lr.setMaterialName(materialService.getMaterialName(material));
        if(StringUtils.isEmpty(materialGroup)) {
            materialGroup = materialService.getMaterialGroup(material);
        }
        lr.setMaterialGroup(materialGroup);
        lr.setMaterialGroupName(materialGroupService.getMaterialGroupName(materialGroup));
        lr.setLossRate(lossRate);
        lr.setCreator(user);
        lr.setUpdater(user);
        lossRateRepository.save(lr);
        log.info("保存料号"+material+"的损耗率 >> END");
    }

    @Override
    public void saveMaterialGroupLossRate(String materialGroup, double lossRate, String user) {
        log.info("保存物料组"+materialGroup+"的损耗率 >> START");
        delMaterialGroupLossRate(materialGroup, user);
        LossRate lr = new LossRate();
        lr.setType(LossRate.type_materialGroup);
        lr.setMaterialGroup(materialGroup);
        lr.setMaterialGroupName(materialGroupService.getMaterialGroupName(materialGroup));
        lr.setLossRate(lossRate);
        lr.setCreator(user);
        lr.setUpdater(user);
        lossRateRepository.save(lr);
        log.info("保存物料组"+materialGroup+"的损耗率 >> END");
    }

    @Override
    public void delMaterialLossRate(String material, String user) {
        log.info("删除料号"+material+"的损耗率");
        LossRate lossRate = getLossRateByMaterial(material);
        if(lossRate == null) return;
        deleteLossRate(lossRate, user);
    }

    @Override
    public void delMaterialGroupLossRate(String materialGroup, String user) {
        log.info("删除物料组"+materialGroup+"的损耗率");
        LossRate lossRate = getLossRateByMaterialGroup(materialGroup);
        if(lossRate == null) return;
        deleteLossRate(lossRate, user);
    }

    @Override
    public double getLossRate(String material) {
        LossRate lossRate = getLossRateByMaterial(material);
        //如果料号没有维护则选择物料组
        if(lossRate == null) {
            String materialGroup = materialService.getMaterialGroup(material);
            lossRate = getLossRateByMaterialGroup(materialGroup);
        }
        if(lossRate == null) return 0D;
        return lossRate.getLossRate();
    }

    @Override
    public Page<LossRate> queryLossRate(int page, int limit, String searchGroup, String searchMaterial) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "materialGroup", "material");
        Specification<LossRate> spec = (Specification<LossRate>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.isNotEmpty(searchGroup)) {
                predicates.add(criteriaBuilder.like(root.get("materialGroup"), searchGroup+"%"));
            }
            if(StringUtils.isNotEmpty(searchMaterial)) {
                predicates.add(criteriaBuilder.like(root.get("material"), searchMaterial+"%"));
            }
            predicates.add(criteriaBuilder.equal(root.get("validFlag"), true));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return lossRateRepository.findAll(spec, pageable);
    }

    @Override
    public List<LossRate> queryLossRate(String searchGroup, String searchMaterial) {
        Sort sort = new Sort(Sort.Direction.ASC, "materialGroup", "material");
        Specification<LossRate> spec = (Specification<LossRate>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.isNotEmpty(searchGroup)) {
                predicates.add(criteriaBuilder.like(root.get("materialGroup"), "%"+searchGroup+"%"));
            }
            if(StringUtils.isNotEmpty(searchMaterial)) {
                predicates.add(criteriaBuilder.like(root.get("material"), "%"+searchMaterial+"%"));
            }
            predicates.add(criteriaBuilder.equal(root.get("validFlag"), true));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return lossRateRepository.findAll(spec, sort);
    }

    @Override
    public void importExcel(InputStream inputStream, String fileName) {
        List<List<Object>> list = ExcelUtil.readExcelFirstSheet(inputStream, fileName);
        String[] titleItems = new String[] {"物料组","物料组名", "料号", "物料名","损耗率"};
        int intRow =1;
        try {
            for(; intRow<list.size(); intRow++) {
                List<Object> dataList = list.get(intRow);
                Map<String, Object> map = new HashMap<>();
                for(int i=0; i<titleItems.length; i++) {
                    if(i==dataList.size()) break;
                    map.put(titleItems[i], dataList.get(i));
                }
                String materialGroup;
                if(map.get("物料组") instanceof BigDecimal) {
                    materialGroup =  (map.get("物料组")).toString();
                } else {
                    materialGroup =  (String) map.get("物料组");
                }
                String material ;
                if(map.get("料号") instanceof BigDecimal) {
                    material =  (map.get("料号")).toString();
                } else {
                    material =  (String) map.get("料号");
                }
                double lossRate = ((BigDecimal) map.get("损耗率")).doubleValue();
                if(StringUtils.isNotEmpty(material)) {
                    saveMaterialLossRate(material, lossRate, "SYS", materialGroup);
                } else if(StringUtils.isNotEmpty(materialGroup)) {
                    saveMaterialGroupLossRate(materialGroup, lossRate, "SYS");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("解析损耗率Excel第"+intRow+"行错误，数据异常", e);
        }
    }

    @Override
    public Workbook exportExcel() {
        List<LossRate> lossRateList = queryLossRate(null, null);
        return writeToExcel(lossRateList);
    }

    @Override
    public Workbook exportExcel(String searchGroup, String searchMaterial) {
        List<LossRate> lossRateList = queryLossRate(searchGroup, searchMaterial);
        return writeToExcel(lossRateList);
    }

    private Workbook writeToExcel(List<LossRate> lossRateList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        String[] titleItems = new String[] {"物料组","物料组名", "料号", "物料名","损耗率（%）"};

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

        for(LossRate lossRate : lossRateList) {
            intRow++;
            intCel = 0;
            Row row = sheet.createRow(intRow);
            Cell cell = row.createCell(intCel++);
            cell.setCellValue(lossRate.getMaterialGroup());
            cell.setCellStyle(cellStyle2);
            row.createCell(intCel++).setCellValue(lossRate.getMaterialGroupName());
            row.createCell(intCel++).setCellValue(lossRate.getMaterial());
            row.createCell(intCel++).setCellValue(lossRate.getMaterialName());
            row.createCell(intCel).setCellValue(lossRate.getLossRate());
        }

        return workbook;
    }

    @Override
    public Workbook downloadExcel() {
        return writeToExcel(new ArrayList<>());
    }
}
