package com.ivo.mrp.service.impl;

import com.ivo.common.utils.ExcelUtil;
import com.ivo.mrp.entity.Substitute;
import com.ivo.mrp.repository.SubstituteRepository;
import com.ivo.mrp.service.MaterialGroupService;
import com.ivo.mrp.service.MaterialService;
import com.ivo.mrp.service.SubstituteService;
import com.ivo.rest.RestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
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
public class SubstituteServiceImpl implements SubstituteService {

    private SubstituteRepository substituteRepository;

    private RestService restService;

    private MaterialService materialService;

    private MaterialGroupService materialGroupService;

    @Autowired
    public SubstituteServiceImpl(SubstituteRepository substituteRepository, RestService restService,
                                 MaterialService materialService, MaterialGroupService materialGroupService) {
        this.substituteRepository = substituteRepository;
        this.restService = restService;
        this.materialService = materialService;
        this.materialGroupService = materialGroupService;
    }

    public Substitute getSubstitute(String fab, String product, String materialGroup, String material) {
        return substituteRepository.findFirstByFabAndProductAndMaterialGroupAndMaterialAndValidFlag(fab, product,
                materialGroup, material, true);
    }

    public List<Substitute> getSubstitute(String fab, String product, String materialGroup) {
        return substituteRepository.findByFabAndProductAndMaterialGroupAndValidFlag(fab, product, materialGroup, true);
    }

    @Override
    public void syncSubstitute() {
        log.info("同步替代料 >> START");
        log.info("LCM1替代料...");
        List<Map> lcm1MapList = restService.getMaterialSubstituteLcm1();
        if(lcm1MapList != null && lcm1MapList.size()>0) {
            convertMapToSubstitute(lcm1MapList);
        }
        log.info("LCM2替代料...");
        List<Map> lcm2MapList = restService.getMaterialSubstituteLcm2();
        if(lcm2MapList != null && lcm2MapList.size()>0) {
            convertMapToSubstitute(lcm2MapList);
        }
        log.info("CELL替代料...");
        List<Map> cellMapList = restService.getMaterialSubstituteCell();
        if(cellMapList != null && cellMapList.size()>0) {
            convertMapToSubstitute(cellMapList);
        }
        log.info("同步替代料 >> END");
    }

    private void convertMapToSubstitute(List<Map> mapList) {
        for(Map map : mapList) {
            String fab = (String) map.get("fab");
            String product = (String) map.get("product");
            String materialGroup = (String) map.get("materialGroup");
            String material = (String) map.get("material");
            Substitute substitute = getSubstitute(fab, product, materialGroup, material);
            if(substitute != null) continue;
            substitute = new Substitute();
            substitute.setFab(fab);
            substitute.setProduct(product);
            substitute.setMaterialGroup(materialGroup);
            substitute.setMaterial(material);
            substitute.setMaterialName((String) map.get("materialName"));
            substitute.setMaterialGroupName((String) map.get("materialGroupName"));
            substitute.setMemo("81数据库同步");
            Object rate = map.get("rate");
            if(rate == null) {
                substitute.setSubstituteRate(100d);
            } else {
                substitute.setSubstituteRate(((BigDecimal) rate).doubleValue());
            }
            substituteRepository.save(substitute);
        }
    }

    @Override
    public Double getSubstituteRate(String fab, String product, String materialGroup, String material) {
        Substitute substitute = getSubstitute(fab, product, materialGroup, material);
        if(substitute == null)
            return null;
        else
            return substitute.getSubstituteRate();
    }

    @Override
    public Page<Substitute> querySubstitute(int page, int limit, String searchFab, String searchProduct, String searchMaterialGroup) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "fab", "product", "materialGroup");
        Specification<Substitute> spec = (Specification<Substitute>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.isNotEmpty(searchFab)) {
                predicates.add(criteriaBuilder.like(root.get("fab"), searchFab+"%"));
            }
            if(StringUtils.isNotEmpty(searchProduct)) {
                predicates.add(criteriaBuilder.like(root.get("product"), searchProduct+"%"));
            }
            if(StringUtils.isNotEmpty(searchMaterialGroup)) {
                predicates.add(criteriaBuilder.like(root.get("materialGroup"), searchMaterialGroup+"%"));

            }
            predicates.add(criteriaBuilder.equal(root.get("validFlag"), true));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return substituteRepository.findAll(spec, pageable);
    }

    @Override
    public List<Substitute> querySubstitute(String searchFab, String searchProduct, String searchMaterialGroup) {
        Sort sort = new Sort(Sort.Direction.ASC, "fab", "product", "materialGroup");
        Specification<Substitute> spec = (Specification<Substitute>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.isNotEmpty(searchFab)) {
                predicates.add(criteriaBuilder.like(root.get("fab"), searchFab+"%"));
            }
            if(StringUtils.isNotEmpty(searchProduct)) {
                predicates.add(criteriaBuilder.like(root.get("product"), searchProduct+"%"));
            }
            if(StringUtils.isNotEmpty(searchMaterialGroup)) {
                predicates.add(criteriaBuilder.like(root.get("materialGroup"), searchMaterialGroup+"%"));

            }
            predicates.add(criteriaBuilder.equal(root.get("validFlag"), true));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return substituteRepository.findAll(spec, sort);
    }

    @Override
    public void saveSubstitute(String fab, String product, String materialGroup, List<Map> mapList, String user) {
        log.info("保存替代料");
        delSubstitute(fab, product, materialGroup, user);
        List<Substitute> substituteList = new ArrayList<>();
        for(Map map : mapList) {
            String material = (String) map.get("material");
            double substituteRate = (Double) map.get("substituteRate");
            Substitute substitute = new Substitute();
            substitute.setFab(fab);
            substitute.setProduct(product);
            substitute.setMaterialGroup(materialGroup);
            substitute.setMaterial(material);
            substitute.setMaterialName(materialService.getMaterialName(material));
            substitute.setMaterialGroupName(materialGroupService.getMaterialGroupName(materialGroup));
            substitute.setSubstituteRate(substituteRate);
            substitute.setCreator(user);
            substitute.setUpdater(user);
            substituteList.add(substitute);
        }
        substituteRepository.saveAll(substituteList);
    }

    @Override
    public void delSubstitute(String fab, String product, String materialGroup,  String user) {
        log.info("删除替代料");
        List<Substitute> substituteList = getSubstitute(fab, product, materialGroup);
        if(substituteList == null || substituteList.size() == 0) return;
        for(Substitute substitute : substituteList) {
            substitute.setValidFlag(false);
            substitute.setUpdater(user);
            substitute.setUpdateDate(new Date());
        }
        substituteRepository.saveAll(substituteList);
    }

    @Override
    public void importExcel(InputStream inputStream, String fileName) {
        List<List<Object>> list = ExcelUtil.readExcelFirstSheet(inputStream, fileName);
        String[] titleItems = new String[] {"厂别","机种","物料组","物料组名","料号","替代比例","物料名"};
        int intRow =1;
        //处理合并
        String fab = null;
        String product = null;
        String materialGroup = null;
        List<Map> mapList = new ArrayList<>();
        try {
            for (; intRow < list.size(); intRow++) {
                List<Object> dataList = list.get(intRow);
                Map<String, Object> map = new HashMap<>();
                for (int i = 0; i < titleItems.length; i++) {
                    if (i == dataList.size()) break;
                    map.put(titleItems[i], dataList.get(i));
                }

                String fab_ = (String) map.get("厂别");
                String product_ = (String) map.get("机种");
                String materialGroup_ = (String) map.get("物料组");
                String material_ = (String) map.get("料号");
                double substituteRate_ = ((BigDecimal) map.get("替代比例")).doubleValue();
                Map<String, Object> map_ = new HashMap<>();
                map_.put("material", material_);
                map_.put("substituteRate", substituteRate_);

                if (fab == null) fab = fab_;
                if (product == null) product = product_;
                if (materialGroup == null) materialGroup = materialGroup_;

                if ((fab + product + materialGroup).equals(fab_ + product_ + materialGroup_)) {
                    mapList.add(map_);
                } else {
                    saveSubstitute(fab, product, materialGroup, mapList, "sys");
                    fab = fab_;
                    product = product_;
                    materialGroup = materialGroup_;
                    mapList = new ArrayList<>();
                    mapList.add(map_);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("解析替代料Excel第"+intRow+"行错误，数据异常", e);
        }
    }

    @Override
    public Workbook exportExcel(String fab, String product, String materialGroup) {
        List<Substitute> substituteList = querySubstitute(fab, product, materialGroup);
        return writeToExcel(substituteList);
    }

    private Workbook writeToExcel(List<Substitute> substituteList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        String[] titleItems = new String[] {"厂别","机种","物料组","物料组名","料号","替代比例（%）","物料名"};

        //首行标题居中、加背景
        CellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.CENTER); //水平居中
        cellStyle1.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex()); //设置背景色
        cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND); //设置加粗
        //设置列宽
        sheet.setColumnWidth(0, 15*256);
        sheet.setColumnWidth(1, 20*256);
        sheet.setColumnWidth(2, 15*256);
        sheet.setColumnWidth(3, 20*256);
        sheet.setColumnWidth(4, 20*256);
        sheet.setColumnWidth(5, 20*256);
        sheet.setColumnWidth(6, 50*256);
        //单元格居中
        CellStyle cellStyle2 = workbook.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.CENTER); //水平居中
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER); //垂直居中

        int intRow = 0;
        int intCel = 0;
        Row row1 = sheet.createRow(intRow);
        for(; intCel<titleItems.length; intCel++) {
            Cell cell = row1.createCell(intCel);
            cell.setCellValue(titleItems[intCel]);
            cell.setCellStyle(cellStyle1);
        }

        //作合并
        int mergeInt = 0;
        String mergeKey = null;
        for(Substitute substitute  : substituteList) {
            //行合并
            String mergeKey_ = substitute.getFab() + substitute.getProduct() + substitute.getMaterialGroup();
            if(mergeKey == null) {
                mergeKey = mergeKey_;
            } else if(mergeKey.equals(mergeKey_)) {
                mergeInt++;
            } else {
                if(mergeInt>0) {
                    sheet.addMergedRegion(new CellRangeAddress(intRow-mergeInt, intRow, 0, 0));
                    sheet.addMergedRegion(new CellRangeAddress(intRow-mergeInt, intRow, 1, 1));
                    sheet.addMergedRegion(new CellRangeAddress(intRow-mergeInt, intRow, 2, 2));
                    sheet.addMergedRegion(new CellRangeAddress(intRow-mergeInt, intRow, 3, 3));

                }
                mergeKey = mergeKey_;
                mergeInt=0;
            }

            intRow++;
            intCel = 0;
            Row row = sheet.createRow(intRow);
            Cell cell1 = row.createCell(intCel++);
            cell1.setCellValue(substitute.getFab());
            cell1.setCellStyle(cellStyle2);

            row.createCell(intCel++).setCellValue(substitute.getProduct());

            Cell cell2 = row.createCell(intCel++);
            cell2.setCellValue(substitute.getMaterialGroup());
            cell2.setCellStyle(cellStyle2);

            row.createCell(intCel++).setCellValue(substitute.getMaterialGroupName());
            row.createCell(intCel++).setCellValue(substitute.getMaterial());
            row.createCell(intCel++).setCellValue(substitute.getSubstituteRate());
            row.createCell(intCel).setCellValue(substitute.getMaterialName());
        }
        if(mergeInt>0) {
            sheet.addMergedRegion(new CellRangeAddress(intRow-mergeInt, intRow, 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(intRow-mergeInt, intRow, 1, 1));
            sheet.addMergedRegion(new CellRangeAddress(intRow-mergeInt, intRow, 2, 2));
            sheet.addMergedRegion(new CellRangeAddress(intRow-mergeInt, intRow, 3, 3));
        }

        return workbook;
    }

    @Override
    public Workbook downloadExcel() {
        return writeToExcel(new ArrayList<>());
    }
}
