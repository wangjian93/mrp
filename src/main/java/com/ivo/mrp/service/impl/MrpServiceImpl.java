package com.ivo.mrp.service.impl;

import com.ivo.common.utils.DateUtil;
import com.ivo.mrp.entity.MrpVer;
import com.ivo.mrp.entity.direct.ary.MrpAry;
import com.ivo.mrp.entity.direct.ary.MrpAryMaterial;
import com.ivo.mrp.entity.direct.cell.MrpCell;
import com.ivo.mrp.entity.direct.cell.MrpCellMaterial;
import com.ivo.mrp.entity.direct.lcm.MrpLcm;
import com.ivo.mrp.entity.direct.lcm.MrpLcmMaterial;
import com.ivo.mrp.entity.packaging.MrpPackage;
import com.ivo.mrp.key.MrpKey;
import com.ivo.mrp.key.MrpMaterialKey;
import com.ivo.mrp.repository.*;
import com.ivo.mrp.service.MrpService;
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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.sql.Date;
import java.util.*;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class MrpServiceImpl implements MrpService {

    private MrpVerRepository mrpVerRepository;

    private MrpLcmMaterialRepository mrpLcmMaterialRepository;

    private MrpAryMaterialRepository mrpAryMaterialRepository;

    private MrpCellMaterialRepository mrpCellMaterialRepository;

    private MrpLcmRepository mrpLcmRepository;

    private MrpAryRepository mrpAryRepository;

    private MrpCellRepository mrpCellRepository;

    private MrpPackageRpository mrpPackageRpository;

    @Autowired
    public MrpServiceImpl(MrpVerRepository mrpVerRepository, MrpLcmMaterialRepository mrpLcmMaterialRepository,
                          MrpAryMaterialRepository mrpAryMaterialRepository, MrpCellMaterialRepository mrpCellMaterialRepository,
                          MrpLcmRepository mrpLcmRepository, MrpAryRepository mrpAryRepository,
                          MrpCellRepository mrpCellRepository,
                          MrpPackageRpository mrpPackageRpository) {
        this.mrpVerRepository = mrpVerRepository;
        this.mrpLcmMaterialRepository = mrpLcmMaterialRepository;
        this.mrpAryMaterialRepository = mrpAryMaterialRepository;
        this.mrpCellMaterialRepository = mrpCellMaterialRepository;
        this.mrpLcmRepository = mrpLcmRepository;
        this.mrpAryRepository = mrpAryRepository;
        this.mrpCellRepository = mrpCellRepository;
        this.mrpPackageRpository = mrpPackageRpository;
    }

    @Override
    public void saveMrpVer(MrpVer mrpVer) {
        mrpVerRepository.save(mrpVer);
    }

    @Override
    public long countMrp() {
        return mrpVerRepository.count();
    }

    @Override
    public MrpVer getMrpVer(String ver) {
        return mrpVerRepository.findById(ver).orElse(null);
    }

    @Override
    public void delMrpVer(String ver) {
        MrpVer mrpVer = getMrpVer(ver);
        mrpVer.setValidFlag(false);
        mrpVer.setUpdateDate(new java.util.Date());
        saveMrpVer(mrpVer);
    }

    @Override
    public String convertAryToString(String[] vers) {
        if(vers == null || vers.length == 0) {
            return null;
        }
        StringBuilder verStr = null;
        for(String ver : vers) {
            if(verStr == null) {
                verStr = new StringBuilder(ver);
            } else {
                verStr.append(",").append(ver);
            }
        }
        return verStr.toString();
    }

    @Override
    public String[] convertStringToAry(String ver) {
        if(ver == null || ver.equals("")) return new String[0];
        return ver.split(",");
    }

    @Override
    public void saveMrpLcmMaterial(List<MrpLcmMaterial> list) {
        mrpLcmMaterialRepository.saveAll(list);
    }

    @Override
    public void saveMrpAryMaterial(List<MrpAryMaterial> list) {
        mrpAryMaterialRepository.saveAll(list);
    }

    @Override
    public void saveMrpCellMaterial(List<MrpCellMaterial> list) {
        mrpCellMaterialRepository.saveAll(list);
    }

    @Override
    public List<MrpLcmMaterial> getMrpLcmMaterial(String ver) {
        return mrpLcmMaterialRepository.findByVer(ver);
    }

    @Override
    public List<MrpAryMaterial> getMrpAryMaterial(String ver) {
        return mrpAryMaterialRepository.findByVer(ver);
    }

    @Override
    public List<MrpCellMaterial> getMrpCellMaterial(String ver) {
        return mrpCellMaterialRepository.findByVer(ver);
    }

    @Override
    public List<String> getMaterialLcm(String ver) {
        return mrpLcmMaterialRepository.getMaterial(ver);
    }

    @Override
    public List<String> getMaterialAry(String ver) {
        return mrpAryMaterialRepository.getMaterial(ver);
    }

    @Override
    public List<String> getMaterialCell(String ver) {
        return mrpCellMaterialRepository.getMaterial(ver);
    }

    @Override
    public MrpLcmMaterial getMrpLcmMaterial(String ver, String material) {
        return mrpLcmMaterialRepository.findById(new MrpMaterialKey(ver, material)).orElse(null);
    }

    @Override
    public MrpAryMaterial getMrpAryMaterial(String ver, String material) {
        return mrpAryMaterialRepository.findById(new MrpMaterialKey(ver, material)).orElse(null);
    }

    @Override
    public MrpCellMaterial getMrpCellMaterial(String ver, String material) {
        return mrpCellMaterialRepository.findById(new MrpMaterialKey(ver, material)).orElse(null);
    }

    @Override
    public List<MrpLcm> getMrpLcm(String ver, String material) {
        return mrpLcmRepository.findByVerAndMaterialOrderByFabDateAsc(ver, material);
    }

    @Override
    public List<MrpAry> getMrpAry(String ver, String material) {
        return mrpAryRepository.findByVerAndMaterialOrderByFabDateAsc(ver, material);
    }

    @Override
    public List<MrpCell> getMrpCell(String ver, String material) {
        return mrpCellRepository.findByVerAndMaterialOrderByFabDateAsc(ver, material);
    }

    @Override
    public List<MrpCell> getMrpCell(String ver) {
        return mrpCellRepository.findByVerOrderByFabDateAsc(ver);
    }

    @Override
    public List<MrpLcm> getMrpLcm(String ver) {
        return mrpLcmRepository.findByVerOrderByMaterialAsc(ver);
    }

    @Override
    public List<MrpAry> getMrpAry(String ver) {
        return mrpAryRepository.findByVerOrderByFabDateAsc(ver);
    }

    @Override
    public List<MrpPackage> getMrpPackage(String ver) {
        Sort sort = new Sort(Sort.Direction.ASC, "product", "type", "linkQty", "mode", "material");
        return mrpPackageRpository.findByVer(ver, sort);
    }

    @Override
    public List<Date> getMrpCalendar(String ver) {
        MrpVer mrpVer = getMrpVer(ver);
        if(mrpVer == null) return new ArrayList<>();
        List<java.util.Date> dateList = DateUtil.getCalendar(mrpVer.getStartDate(), mrpVer.getEndDate());
        List<Date> dates = new ArrayList<>();
        for(java.util.Date d : dateList) {
            dates.add(new Date(d.getTime()));
        }
        return dates;
    }

    @Override
    public void saveMrpLcm(List<MrpLcm> list) {
        mrpLcmRepository.saveAll(list);
    }

    @Override
    public void saveMrpAry(List<MrpAry> list) {
        mrpAryRepository.saveAll(list);
    }

    @Override
    public void saveMrpCell(List<MrpCell> list) {
        mrpCellRepository.saveAll(list);
    }

    @Override
    public List<MrpPackage> getMrpPackage(String ver, String product, String type, Double linkQty, String mode) {
        return mrpPackageRpository.findByVerAndProductAndTypeAndLinkQtyAndMode(ver, product, type, linkQty, mode);
    }

    @Override
    public void saveMrpPackage(List<MrpPackage> list) {
        mrpPackageRpository.saveAll(list);
    }

    @Override
    public void deleteMrpPackage(List<MrpPackage> list) {
        mrpPackageRpository.deleteAll(list);
    }

    @Override
    public Page<MrpVer> queryMrpVer(int page, int limit, String searchFab, String searchType, String searchVer) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.DESC, "ver");
        return mrpVerRepository.findByFabLikeAndTypeLikeAndVerLikeAndValidFlagIsTrue(searchFab+"%", searchType+"%", searchVer+"%", pageable);
    }

    @Override
    public List getMrpDate(String ver) {
        MrpVer mrpVer = getMrpVer(ver);
        if(mrpVer == null) return new ArrayList();
        String type = mrpVer.getType();
        List list;
        switch (type) {
            case MrpVer.Type_Ary:
                list = getMrpAry(ver);
                break;
            case MrpVer.Type_Cell:
                list = getMrpCell(ver);
                break;
            case MrpVer.Type_Lcm:
                list = getMrpLcm(ver);
                break;
            case MrpVer.Type_Package:
                list = getMrpPackage(ver);
                break;
            default:
                list = new ArrayList();
        }
        return list;
    }

    @Override
    public Page<MrpLcmMaterial> getPageMrpLcmMaterial(int page, int limit, String ver, String searchProduct,
                                                      String searchMaterialGroup, String searchMaterial,
                                                      String searchSupplier) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "materialGroup", "material");
        Specification<MrpLcmMaterial> spec = (Specification<MrpLcmMaterial>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("ver"), ver));
            if(StringUtils.isNotEmpty(searchProduct)) {
                predicates.add(criteriaBuilder.like(root.get("products"), "%"+searchProduct+"%"));
            }
            if(StringUtils.isNotEmpty(searchMaterialGroup)) {
                predicates.add(criteriaBuilder.like(root.get("materialGroup"), searchMaterialGroup+"%"));
            }
            if(StringUtils.isNotEmpty(searchMaterial)) {
                predicates.add(criteriaBuilder.like(root.get("material"), searchMaterial+"%"));
            }
            if(StringUtils.isNotEmpty(searchSupplier)) {
                predicates.add(criteriaBuilder.like(root.get("suppliers"), "%"+searchSupplier+"%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return mrpLcmMaterialRepository.findAll(spec, pageable);
    }

    @Override
    public List<MrpLcm> getMrpLcm(String ver, List<String> materialList) {
        return mrpLcmRepository.findByVerAndMaterialInOrderByMaterialAsc(ver, materialList);
    }

    @Override
    public MrpLcm getMrpLcm(String ver, String material, Date fabDate) {
        return mrpLcmRepository.findById(new MrpKey(ver, fabDate, material)).orElse(null);
    }

    @Override
    public MrpCell getMrpCell(String ver, String material, Date fabDate) {
        return mrpCellRepository.findById(new MrpKey(ver, fabDate, material)).orElse(null);
    }

    @Override
    public MrpAry getMrpAry(String ver, String material, Date fabDate) {
        return mrpAryRepository.findById(new MrpKey(ver, fabDate, material)).orElse(null);
    }

    @Override
    public Page<MrpCellMaterial> getPageMrpCellMaterial(int page, int limit, String ver, String searchProduct, String searchMaterialGroup, String searchMaterial, String searchSupplier) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "materialGroup", "material");
        Specification<MrpCellMaterial> spec = (Specification<MrpCellMaterial>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("ver"), ver));
            if(StringUtils.isNotEmpty(searchProduct)) {
                predicates.add(criteriaBuilder.like(root.get("products"), "%"+searchProduct+"%"));
            }
            if(StringUtils.isNotEmpty(searchMaterialGroup)) {
                predicates.add(criteriaBuilder.like(root.get("materialGroup"), searchMaterialGroup+"%"));
            }
            if(StringUtils.isNotEmpty(searchMaterial)) {
                predicates.add(criteriaBuilder.like(root.get("material"), searchMaterial+"%"));
            }
            if(StringUtils.isNotEmpty(searchSupplier)) {
                predicates.add(criteriaBuilder.like(root.get("suppliers"), "%"+searchSupplier+"%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return mrpCellMaterialRepository.findAll(spec, pageable);
    }

    @Override
    public Page<MrpAryMaterial> getPageMrpAryMaterial(int page, int limit, String ver, String searchProduct, String searchMaterialGroup, String searchMaterial, String searchSupplier) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "materialGroup", "material");
        Specification<MrpAryMaterial> spec = (Specification<MrpAryMaterial>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("ver"), ver));
            if(StringUtils.isNotEmpty(searchProduct)) {
                predicates.add(criteriaBuilder.like(root.get("products"), "%"+searchProduct+"%"));
            }
            if(StringUtils.isNotEmpty(searchMaterialGroup)) {
                predicates.add(criteriaBuilder.like(root.get("materialGroup"), searchMaterialGroup+"%"));
            }
            if(StringUtils.isNotEmpty(searchMaterial)) {
                predicates.add(criteriaBuilder.like(root.get("material"), searchMaterial+"%"));
            }
            if(StringUtils.isNotEmpty(searchSupplier)) {
                predicates.add(criteriaBuilder.like(root.get("suppliers"), "%"+searchSupplier+"%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return mrpAryMaterialRepository.findAll(spec, pageable);
    }

    @Override
    public Workbook exportMrp(String ver, String product, String materialGroup, String material, String supplier) {

        String[] titleItems = new String[] {"料号", "机种", "供应商","物料名","物料组","物料组名","厂别","损耗率","良品库存","呆滞库存",""};
        List list = getMrpCalendar(ver);
        List list1 = DateUtil.format_(list);
        List list2 = DateUtil.getMonthBetween((Date) list.get(0), (Date) list.get(list.size()-1));
        String[] days = (String[]) list1.toArray(new String[list1.size()]);
        String[] weeks = DateUtil.getWeekDay_(list);
        String[] months = (String[]) list2.toArray(new String[list2.size()]);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        int intRow =0;
        //标题行
        //标题居中、加背景
        CellStyle titleCellStyle = workbook.createCellStyle();
        titleCellStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
        titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER); //垂直居中
        titleCellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex()); //设置背景色
        titleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); //设置加粗

        Row row0 = sheet.createRow(intRow++);
        Row row1 = sheet.createRow(intRow++);
        int intCel = 0;
        for (String titleItem : titleItems) {
            Cell cell = row0.createCell(intCel);
            cell.setCellValue(titleItem);
            cell.setCellStyle(titleCellStyle);

            //标题行合并
            sheet.addMergedRegion(new CellRangeAddress(0,  1, intCel, intCel));
            intCel++;
        }
        for (String month : months) {
            Cell cell = row0.createCell(intCel);
            month = month.replace("-","年")+"月";
            cell.setCellValue(month);
            cell.setCellStyle(titleCellStyle);

            //标题行合并
            sheet.addMergedRegion(new CellRangeAddress(0,  1, intCel, intCel));
            intCel++;
        }
        for (int i=0; i<days.length; i++) {
            Cell cell1 = row0.createCell(intCel);
            cell1.setCellValue(days[i]);
            cell1.setCellStyle(titleCellStyle);

            Cell cell2 = row1.createCell(intCel);
            cell2.setCellValue(weeks[i]);
            cell2.setCellStyle(titleCellStyle);
            intCel++;
        }

        //设置列宽
        for(int i=0; i<titleItems.length+months.length+weeks.length; i++) {
            sheet.setColumnWidth(i, 15*256);
        }
        sheet.setColumnWidth(1, 15*256*2);  //机种、供应商宽度加倍
        sheet.setColumnWidth(2, 15*256*2);

        //日期对应的列
        Map<String, Integer> fabDateMap = new HashMap<>();
        for(int i=0; i<days.length; i++) {
            fabDateMap.put(days[i], titleItems.length+months.length+i);
        }

        MrpVer mrpVer = getMrpVer(ver);

        List<Object> mrpMaterialList = new ArrayList<>();
        if(mrpVer.getType().equals(MrpVer.Type_Lcm)) {
            Page p =  getPageMrpLcmMaterial(0, 5000, ver, product, materialGroup, material, supplier);
            mrpMaterialList = p.getContent();
        } else if(mrpVer.getType().equals(MrpVer.Type_Cell)) {
            Page p =  getPageMrpCellMaterial(0, 5000, ver, product, materialGroup, material, supplier);
            mrpMaterialList = p.getContent();
        } else if(mrpVer.getType().equals(MrpVer.Type_Ary)) {
            Page p =  getPageMrpAryMaterial(0, 5000, ver, product, materialGroup, material, supplier);
            mrpMaterialList = p.getContent();
        }
        //单元格居中
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); //垂直居中
        cellStyle.setWrapText(true); //自动换行
        int l = mrpMaterialList.size();
        for(Object object : mrpMaterialList) {
            System.out.println("剩余"+l--);
            Map<String, Object> map = new HashMap<>();
            String m = null;
            if(object instanceof MrpLcmMaterial) {
                m = ((MrpLcmMaterial) object).getMaterial();
                map.put("料号", m);
                map.put("机种", ((MrpLcmMaterial) object).getProducts().replace(",", "\r\n"));
                map.put("供应商", ((MrpLcmMaterial) object).getSuppliers().replace(",", "\r\n"));
                map.put("物料名", ((MrpLcmMaterial) object).getMaterialGroupName());
                map.put("物料组", ((MrpLcmMaterial) object).getMaterialGroup());
                map.put("物料组名", ((MrpLcmMaterial) object).getMaterialGroupName());
                map.put("厂别", ((MrpLcmMaterial) object).getFab());
                map.put("损耗率", ((MrpLcmMaterial) object).getLossRate());
                map.put("良品库存", ((MrpLcmMaterial) object).getGoodInventory());
                map.put("呆滞库存", ((MrpLcmMaterial) object).getDullInventory());
            } else if(object instanceof MrpCellMaterial) {
                m = ((MrpCellMaterial) object).getMaterial();
                map.put("料号", m);
                map.put("机种", ((MrpCellMaterial) object).getProducts().replace(",", "\r\n"));
                map.put("供应商", ((MrpCellMaterial) object).getSuppliers().replace(",", "\r\n"));
                map.put("物料名", ((MrpCellMaterial) object).getMaterialGroupName());
                map.put("物料组", ((MrpCellMaterial) object).getMaterialGroup());
                map.put("物料组名", ((MrpCellMaterial) object).getMaterialGroupName());
                map.put("厂别", ((MrpCellMaterial) object).getFab());
                map.put("损耗率", ((MrpCellMaterial) object).getLossRate());
                map.put("良品库存", ((MrpCellMaterial) object).getGoodInventory());
                map.put("呆滞库存", ((MrpCellMaterial) object).getDullInventory());
            } else if(object instanceof MrpAryMaterial) {
                m = ((MrpAryMaterial) object).getMaterial();
                map.put("料号", m);
                map.put("机种", ((MrpAryMaterial) object).getProducts().replace(",", "\r\n"));
                map.put("供应商", ((MrpAryMaterial) object).getSuppliers().replace(",", "\r\n"));
                map.put("物料名", ((MrpAryMaterial) object).getMaterialGroupName());
                map.put("物料组", ((MrpAryMaterial) object).getMaterialGroup());
                map.put("物料组名", ((MrpAryMaterial) object).getMaterialGroupName());
                map.put("厂别", ((MrpAryMaterial) object).getFab());
                map.put("损耗率", ((MrpAryMaterial) object).getLossRate());
                map.put("良品库存", ((MrpAryMaterial) object).getGoodInventory());
                map.put("呆滞库存", ((MrpAryMaterial) object).getDullInventory());
            } else {
                continue;
            }

            Row row1_demand = sheet.createRow(intRow++);
            Row row2_loss = sheet.createRow(intRow++);
            Row row3_arrival = sheet.createRow(intRow++);
            Row row4_balance = sheet.createRow(intRow++);
//            Row row5_short = sheet.createRow(intRow++);
//            Row row6_allocation = sheet.createRow(intRow++);
            intCel =0;
            for (String titleItem : titleItems) {
                if(intCel < titleItems.length-1) {
                    Object o = map.get(titleItem);
                    Cell cell = row1_demand.createCell(intCel);
                    cell.setCellValue(o == null ? "" : o.toString());
                    cell.setCellStyle(cellStyle);
                    //合并
                    sheet.addMergedRegion(new CellRangeAddress(intRow-4,  intRow-1, intCel, intCel));
                } else {
                    row1_demand.createCell(intCel).setCellValue("需求量");
                    row2_loss.createCell(intCel).setCellValue("损耗量");
                    row3_arrival.createCell(intCel).setCellValue("到货量");
                    row4_balance.createCell(intCel).setCellValue("结余量");
//                    row5_short.createCell(intCel).setCellValue("缺料量");
//                    row6_allocation.createCell(intCel).setCellValue("分配量");
                }
                intCel++;
            }



            //MRP数据
            List mrpList = new ArrayList<>();
            if(mrpVer.getType().equals(MrpVer.Type_Lcm)) {
                mrpList = getMrpLcm(ver, m);
            } else if(mrpVer.getType().equals(MrpVer.Type_Cell)) {
                mrpList = getMrpCell(ver, m);
            } else if(mrpVer.getType().equals(MrpVer.Type_Ary)) {
                mrpList = getMrpAry(ver, m);
            }
            for(Object mrp : mrpList) {
                if(mrp instanceof MrpLcm) {
                    Date fabDate = ((MrpLcm) mrp).getFabDate();
                    Integer i = fabDateMap.get(fabDate.toString());
                    if(i==null) continue;
                    row1_demand.createCell(i).setCellValue(((MrpLcm) mrp).getDemandQty());
                    row2_loss.createCell(i).setCellValue(((MrpLcm) mrp).getLossQty());
                    row3_arrival.createCell(i).setCellValue(((MrpLcm) mrp).getArrivalQty());
                    row4_balance.createCell(i).setCellValue(((MrpLcm) mrp).getBalanceQty());
//                    row5_short.createCell(i).setCellValue(((MrpLcm) mrp).getShortQty());
//                    row6_allocation.createCell(i).setCellValue(((MrpLcm) mrp).getAllocationQty());
                } else if(mrp instanceof MrpCell) {
                    Date fabDate = ((MrpCell) mrp).getFabDate();
                    Integer i = fabDateMap.get(fabDate.toString());
                    if(i==null) continue;
                    row1_demand.createCell(i).setCellValue(((MrpCell) mrp).getDemandQty());
                    row2_loss.createCell(i).setCellValue(((MrpCell) mrp).getLossQty());
                    row3_arrival.createCell(i).setCellValue(((MrpCell) mrp).getArrivalQty());
                    row4_balance.createCell(i).setCellValue(((MrpCell) mrp).getBalanceQty());
//                    row5_short.createCell(i).setCellValue(((MrpCell) mrp).getShortQty());
//                    row6_allocation.createCell(i).setCellValue(((MrpCell) mrp).getAllocationQty());
                } else if(mrp instanceof MrpAry) {
                    Date fabDate = ((MrpAry) mrp).getFabDate();
                    Integer i = fabDateMap.get(fabDate.toString());
                    if(i==null) continue;
                    row1_demand.createCell(i).setCellValue(((MrpAry) mrp).getDemandQty());
                    row2_loss.createCell(i).setCellValue(((MrpAry) mrp).getLossQty());
                    row3_arrival.createCell(i).setCellValue(((MrpAry) mrp).getArrivalQty());
                    row4_balance.createCell(i).setCellValue(((MrpAry) mrp).getBalanceQty());
//                    row5_short.createCell(i).setCellValue(((MrpAry) mrp).getShortQty());
//                    row6_allocation.createCell(i).setCellValue(((MrpAry) mrp).getAllocationQty());
                }
            }
        }
        return workbook;
    }


    @Override
    public Page<MrpLcmMaterial> getPageMrpLcmMaterial(int page, int limit, String[] vers, String searchProduct,
                                                      String searchMaterialGroup, String searchMaterial,
                                                      String searchSupplier) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "materialGroup", "material");
        Specification<MrpLcmMaterial> spec = (Specification<MrpLcmMaterial>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Path<Object> path = root.get("ver");
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
            for (String ver:vers){
                in.value(ver);
            }
            predicates.add(criteriaBuilder.and(in));
            if(StringUtils.isNotEmpty(searchProduct)) {
                predicates.add(criteriaBuilder.like(root.get("products"), "%"+searchProduct+"%"));
            }
            if(StringUtils.isNotEmpty(searchMaterialGroup)) {
                predicates.add(criteriaBuilder.like(root.get("materialGroup"), searchMaterialGroup+"%"));
            }
            if(StringUtils.isNotEmpty(searchMaterial)) {
                predicates.add(criteriaBuilder.like(root.get("material"), searchMaterial+"%"));
            }
            if(StringUtils.isNotEmpty(searchSupplier)) {
                predicates.add(criteriaBuilder.like(root.get("suppliers"), "%"+searchSupplier+"%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return mrpLcmMaterialRepository.findAll(spec, pageable);
    }
}
