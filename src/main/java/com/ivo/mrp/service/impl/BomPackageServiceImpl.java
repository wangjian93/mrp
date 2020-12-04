package com.ivo.mrp.service.impl;

import com.ivo.common.utils.DoubleUtil;
import com.ivo.common.utils.ExcelUtil;
import com.ivo.mrp.entity.packaging.BomPackage;
import com.ivo.mrp.entity.packaging.BomPackageMaterial;
import com.ivo.mrp.repository.BomPackageRepository;
import com.ivo.mrp.service.BomPackageService;
import com.ivo.mrp.service.MaterialGroupService;
import com.ivo.mrp.service.MaterialService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class BomPackageServiceImpl implements BomPackageService {

    private BomPackageRepository bomPackageRepository;

    private MaterialService materialService;

    private MaterialGroupService materialGroupService;

    @Autowired
    public BomPackageServiceImpl(BomPackageRepository bomPackageRepository, MaterialService materialService,
                                 MaterialGroupService materialGroupService) {
        this.bomPackageRepository = bomPackageRepository;
        this.materialService = materialService;
        this.materialGroupService = materialGroupService;
    }

    @Override
    public List<BomPackage> getBomPackage(String product) {
        return bomPackageRepository.findByProduct(product);
    }

    @Override
    public BomPackage getBomPackage(String product, String type, Double linkQty, String mode) {
        return bomPackageRepository.findFirstByProductAndTypeAndLinkQtyAndMode(product, type, linkQty, mode);
    }

    @Override
    public void saveBomPackage(BomPackage bomPackage) {
        log.info("机种"+bomPackage.getProduct()+"包材BOM保存 >> START");
        //数据校验
        if(StringUtils.isEmpty(bomPackage.getProduct()) || StringUtils.isEmpty(bomPackage.getType())) {
            throw new RuntimeException("包材BOM保存失败，机种和单片/连片类型不能为空");
        }
        if(BomPackage.TYPE_D.equals(bomPackage.getType())) {  //单片
            bomPackage.setLinkQty(null);
            if(StringUtils.isEmpty(bomPackage.getMode()) ||
                    !StringUtils.containsAny(bomPackage.getMode(), "抽", "全切单")) {
                log.warn("包材BOM保存失败，机种"+bomPackage.getProduct()+"的单片需要维护抽单模式(例'全切单'或'600抽3')");
                return;
            }
            if(bomPackage.getCutQty() == null) {
                log.warn("包材BOM保存失败，机种" + bomPackage.getProduct() + "切片数数目不能为空')");
                return;
            }
        } else {   //连片
            bomPackage.setMode(null);
            if (bomPackage.getLinkQty() == null) {
                log.warn("包材BOM保存失败，机种" + bomPackage.getProduct() + "的连片需要维护连片数')");
                return;
            }
            if (bomPackage.getMiddleQty() == null) {
                log.warn("包材BOM保存失败，机种" + bomPackage.getProduct() + "的连片需要中板数')");
                return;
            }
            if(bomPackage.getPanelQty() == null) {
                log.warn("包材BOM保存失败，机种" + bomPackage.getProduct() + "Panel数目不能为空')");
                return;
            }
        }

        for(BomPackageMaterial bomPackageMaterial : bomPackage.getMaterialList()) {
            String material = bomPackageMaterial.getMaterial();
            String materialGroup = materialService.getMaterialGroup(material);
            bomPackageMaterial.setMaterialName(materialService.getMaterialName(material));
            bomPackageMaterial.setMaterialGroup(materialGroup);
            bomPackageMaterial.setMaterialGroupName(materialGroupService.getMaterialGroupName(materialGroup));

            //单耗量（连片）
            //包装规格/连片数/Panel数目
            if(BomPackage.TYPE_L.equals(bomPackage.getType())) {
                Double specQty = bomPackageMaterial.getSpecQty();
                Double linkQty = bomPackage.getLinkQty();
                Double panelQty = bomPackage.getPanelQty();
                Double consumeQty = DoubleUtil.divide(specQty, linkQty);
                consumeQty = DoubleUtil.divide(consumeQty, panelQty);
                bomPackageMaterial.setConsumeQty(consumeQty);
            }
        }

        BomPackage oldBomPackage = getBomPackage(bomPackage.getProduct(), bomPackage.getType(),
                bomPackage.getLinkQty(), bomPackage.getMode());
        //删除就记录
        if(oldBomPackage != null) {
            bomPackageRepository.delete(oldBomPackage);
        }
        bomPackage.setCreator("SYS");
        bomPackageRepository.save(bomPackage);
        log.info("机种"+bomPackage.getProduct()+"包材BOM保存 >> END");
    }

    @Override
    public void importBomPackage(InputStream inputStream, String fileName) {
        log.info("包材BOM数据导入 >> START");
        String[] titleNames = new String[] {"机种", "单片/连片", "连片数", "抽单模式", "切数", "中板数", "单耗量", "品名",
                "料号", "包装规格", "损耗率", "Panel数目", "供应商标识", "需求量"};
        List<List<Object>> list = ExcelUtil.readExcelFirstSheet(inputStream, fileName);
        if(list == null || list.size() < 1) return;
        int rowInt = 1;
        int cellInt = 0;

        List<BomPackage> bomPackageList = new ArrayList<>();
        Map<String, Integer> keyMap = new HashMap<>();
        String product_ = null;
        String type_ = null;
        Double linkQty_ = null;
        String mode_ = null;
        try {
            for(; rowInt<list.size(); rowInt++) {
                if(rowInt == 1749) {
                    System.out.println("");
                }
                List<Object> row = list.get(rowInt);
                Map<String, Object> map = new HashMap<>();
                for(; cellInt<row.size(); cellInt++) {
                    if(cellInt >= titleNames.length) break;
                    map.put(titleNames[cellInt], row.get(cellInt));
                }
                String product = (String) map.get("机种");
                if(product != null) {
                    product = product.trim().toUpperCase();
                }
                String type = (String) map.get("单片/连片");
                if(type != null) {
                    type = type.trim();
                }
                Double linkQty = DoubleUtil.converDouble(map.get("连片数"));
                String mode = (String) map.get("抽单模式");
                if(mode != null) {
                    mode = mode.trim();
                }
                Double cutQty = DoubleUtil.converDouble(map.get("切数"));
                Double middleQty = DoubleUtil.converDouble(map.get("中板数"));
                String consumeStr = (String) map.get("单耗量");
                String material;
                if(map.get("料号") instanceof BigDecimal) {
                    material = ((BigDecimal) map.get("料号")).toString();
                } else {
                    material =  (String) map.get("料号");
                }
                Double specQty = DoubleUtil.converDouble(map.get("包装规格"));
                Double lossRate = DoubleUtil.converDouble(map.get("损耗率"));
                Double panelQty = DoubleUtil.converDouble(map.get("Panel数目"));
                boolean supplierFlag = false;
                Object o =  map.get("供应商标识");
                if(o != null) {
                    if((o.toString()).equals("1")) {
                        supplierFlag = true;
                    }
                }
                String demandStr = (String) map.get("需求量");

                //处理单元格合并的情况
                if(StringUtils.isNotEmpty(product)) {
                    product_ = product;
                    type_ = type;
                    linkQty_ = linkQty;
                    mode_ = mode;
                } else {
                    product = product_;
                    type = type_;
                    linkQty = linkQty_;
                    mode = mode_;
                }

                String key = product+type+linkQty+mode;
                BomPackage bomPackage;
                if(keyMap.get(key) != null) {
                    bomPackage = bomPackageList.get(keyMap.get(key));
                } else {
                    bomPackage = new BomPackage();
                    bomPackage.setProduct(product);
                    bomPackage.setType(type);
                    bomPackage.setLinkQty(linkQty);
                    bomPackage.setMode(mode);
                    bomPackage.setCutQty(cutQty);
                    bomPackage.setMiddleQty(middleQty);
                    bomPackage.setPanelQty(panelQty);
                    bomPackage.setCreator("SYS");
                    bomPackageList.add(bomPackage);
                    keyMap.put(key, bomPackageList.size()-1);
                }
                BomPackageMaterial bomPackageMaterial = new BomPackageMaterial();
                bomPackageMaterial.setBomPackage(bomPackage);
                bomPackageMaterial.setMaterial(material);
                bomPackageMaterial.setConsumeStr(consumeStr);
                bomPackageMaterial.setSpecQty(specQty);
                bomPackageMaterial.setLossRate(lossRate);
                bomPackageMaterial.setSupplierFlag(supplierFlag);
                bomPackageMaterial.setDemandStr(demandStr);
                bomPackageMaterial.setBomPackage(bomPackage);
                bomPackage.getMaterialList().add(bomPackageMaterial);
                cellInt = 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("EXCEL导入错误，第"+rowInt+"行，第"+cellInt+"列数据不准确."+e.getMessage());
        }

        saveBomPackage(bomPackageList);
        log.info("包材BOM数据导入 >> END");
    }

    private void saveBomPackage(List<BomPackage> bomPackageList) {
        for(BomPackage bomPackage : bomPackageList) {
            saveBomPackage(bomPackage);
        }
    }

    @Override
    public List<String> getPackageProduct() {
        return bomPackageRepository.getPackageProduct();
    }

    @Override
    public Page<BomPackage> queryBomPackage(int page, int limit, String searchProduct) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "product");
        return bomPackageRepository.findByProductLike(searchProduct+"%", pageable);
    }
}
