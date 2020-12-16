package com.ivo.mrp.service.packageing;

import com.ivo.common.utils.ExcelUtil;
import com.ivo.mrp.entity.packaging.BomPackage;
import com.ivo.mrp.entity.packaging.BomPackageMaterial;
import com.ivo.mrp.repository.packaging.BomPackageMaterialRepository;
import com.ivo.mrp.repository.packaging.BomPackageRepository;
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

    private BomPackageMaterialRepository bomPackageMaterialRepository;

    private MaterialService materialService;

    private MaterialGroupService materialGroupService;

    @Autowired
    public BomPackageServiceImpl(BomPackageRepository bomPackageRepository,
                                 BomPackageMaterialRepository bomPackageMaterialRepository,
                                 MaterialService materialService, MaterialGroupService materialGroupService) {
        this.bomPackageRepository = bomPackageRepository;
        this.bomPackageMaterialRepository = bomPackageMaterialRepository;
        this.materialService = materialService;
        this.materialGroupService = materialGroupService;
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
        List<BomPackageMaterial> bomPackageMaterialList = new ArrayList<>();
        Map<String, Integer> keyMap = new HashMap<>();
        String product_ = null;
        String type_ = null;
        String linkQty_ = null;
        String mode_ = null;
        try {
            for(; rowInt<list.size(); rowInt++) {
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
                String linkQty = map.get("连片数") == null ? null : map.get("连片数").toString();
                if(linkQty != null) {
                    linkQty = linkQty.trim();
                }
                String mode = map.get("抽单模式") == null ? null : map.get("抽单模式").toString();
                if(mode != null) {
                    mode = mode.trim();
                }
                String cutQty = map.get("切数") == null ? null : map.get("切数").toString();
                String middleQty = map.get("中板数") == null ? null : map.get("中板数").toString();
                String consumeStr = map.get("单耗量") == null ? null : (String) map.get("单耗量");
                String material =  map.get("料号") == null ? null : map.get("料号").toString();

                String specQty;
                if(map.get("包装规格") == null) {
                    specQty = null;
                } else {
                    if(map.get("包装规格") instanceof BigDecimal) {
                        //保留4位小数
                        specQty = String.valueOf( ((BigDecimal)map.get("包装规格")).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue() );
                    } else {
                        specQty = map.get("包装规格").toString();
                    }
                }
                String lossRate = map.get("损耗率") == null ? null : map.get("损耗率").toString();
                String panelQty = map.get("Panel数目") == null ? null : map.get("Panel数目").toString();
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
                    bomPackage.setId(bomPackage.generateId());
                    bomPackageList.add(bomPackage);
                    keyMap.put(key, bomPackageList.size()-1);
                }
                BomPackageMaterial bomPackageMaterial = new BomPackageMaterial();
                bomPackageMaterial.setPackageId(bomPackage.getId());
                bomPackageMaterial.setMaterial(material);
                bomPackageMaterial.setMaterialName(materialService.getMaterialName(material));
                String materialGroup = materialService.getMaterialGroup(material);
                bomPackageMaterial.setMaterialGroup(materialGroup);
                if(materialGroup != null) {
                    bomPackageMaterial.setMaterialGroupName(materialGroupService.getMaterialGroupName(materialGroup));
                }
                bomPackageMaterial.setSpecQty(specQty);
                bomPackageMaterial.setLossRate(lossRate);
                bomPackageMaterial.setSupplierFlag(supplierFlag);
                bomPackageMaterialList.add(bomPackageMaterial);
                cellInt = 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("EXCEL导入错误，第"+rowInt+"行，第"+cellInt+"列数据不准确."+e.getMessage());
        }

        bomPackageRepository.saveAll(bomPackageList);
        bomPackageMaterialRepository.saveAll(bomPackageMaterialList);
        log.info("包材BOM数据导入 >> END");
    }

    @Override
    public Page<BomPackage> queryBomPackage(int page, int limit, String searchProduct) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "product");
        return bomPackageRepository.findByProductLike(searchProduct+"%", pageable);
    }

    @Override
    public BomPackage getBomPackageById(String id) {
        return bomPackageRepository.findById(id).orElse(null);
    }

    @Override
    public List<BomPackageMaterial> getBomPackageMaterial(String packageId) {
        return bomPackageMaterialRepository.findByPackageId(packageId);
    }

    @Override
    public List<BomPackage> getBomPackage(String product) {
        return bomPackageRepository.findByProduct(product);
    }

    @Override
    public List<String> getPackageProduct() {
        return bomPackageRepository.getPackageProduct();
    }

    @Override
    public BomPackage getBomPackage(String product, String type, String linkQty) {
        String id;
        if(type.equals(BomPackage.TYPE_D)) {
            id = product+"_"+type;
        } else {
            id = product+"_"+type+"_"+linkQty;
        }
       return getBomPackageById(id);
    }

    @Override
    public List<BomPackageMaterial> getBomPackageMaterial(String product, String type, String linkQty) {
        String id;
        if(type.equals(BomPackage.TYPE_D)) {
            id = product+"_"+type;
        } else {
            id = product+"_"+type+"_"+linkQty;
        }
        return getBomPackageMaterial(id);
    }

    @Override
    public List<BomPackageMaterial> getBomPackageForSupplier(String packageId) {
        return bomPackageMaterialRepository.findByPackageIdAndSupplierFlagIsTrue(packageId);
    }
}
