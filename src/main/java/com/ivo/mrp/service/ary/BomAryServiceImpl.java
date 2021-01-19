package com.ivo.mrp.service.ary;

import com.ivo.common.utils.DoubleUtil;
import com.ivo.mrp.entity.direct.ary.AryMpsMode;
import com.ivo.mrp.entity.direct.ary.BomAry;
import com.ivo.mrp.entity.direct.ary.BomAryProduct;
import com.ivo.mrp.entity.direct.cell.CellMaterial;
import com.ivo.mrp.repository.ary.AryMpsModeRepository;
import com.ivo.mrp.repository.ary.BomAryProductRepository;
import com.ivo.mrp.repository.ary.BomAryRepository;
import com.ivo.mrp.service.MaterialGroupService;
import com.ivo.mrp.service.MaterialService;
import com.ivo.mrp.service.cell.CellMaterialService;
import com.ivo.rest.RestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
public class BomAryServiceImpl implements BomAryService {

    private RestService restService;

    private AryMpsModeRepository aryMpsModeRepository;

    private BomAryProductRepository bomAryProductRepository;

    private BomAryRepository bomAryRepository;

    private MaterialService materialService;

    private MaterialGroupService materialGroupService;

    private CellMaterialService cellMaterialService;

    public BomAryServiceImpl(RestService restService, AryMpsModeRepository aryMpsModeRepository,
                             BomAryProductRepository bomAryProductRepository, BomAryRepository bomAryRepository,
                             MaterialService materialService,
                             MaterialGroupService materialGroupService,
                             CellMaterialService cellMaterialService) {
        this.restService = restService;
        this.aryMpsModeRepository = aryMpsModeRepository;
        this.bomAryProductRepository = bomAryProductRepository;
        this.bomAryRepository = bomAryRepository;
        this.materialService = materialService;
        this.materialGroupService = materialGroupService;
        this.cellMaterialService = cellMaterialService;
    }

    @Override
    public void syncAryMpsMode() {
        log.info("同步Ary的MPS机种命名 >> START");
        List<Map> mapList = restService.getAryMpsMode();
        if(mapList==null || mapList.size()==0) return;
        List<AryMpsMode> aryMpsModeList = new ArrayList<>();
        for(Map map : mapList) {
            AryMpsMode aryMpsMode = new AryMpsMode();
            aryMpsMode.setProduct((String) map.get("product"));
            aryMpsMode.setCellMtrl((String) map.get("Material_FK"));
            aryMpsMode.setId(aryMpsMode.getProduct()+"_"+aryMpsMode.getCellMtrl());
            aryMpsModeList.add(aryMpsMode);
        }
        aryMpsModeRepository.saveAll(aryMpsModeList);
        log.info("同步Ary的MPS机种命名 >> END");
    }

    @Override
    public void syncBomAryProduct() {
        List<String> productList = aryMpsModeRepository.getProduct();
        if(productList == null || productList.size() == 0) return;
        List<BomAryProduct> bomAryProductList = new ArrayList<>();
        for(String product : productList) {
            BomAryProduct bomAryProduct = getBomAryProduct(product);
            if(bomAryProduct == null) {
                bomAryProduct = new BomAryProduct();
                bomAryProduct.setProduct(product);
                bomAryProduct.setVerify(false);
                bomAryProductList.add(bomAryProduct);
            }
        }
        bomAryProductRepository.saveAll(bomAryProductList);
    }

    @Override
    public void syncBomAry() {
        List<BomAryProduct> bomAryProductList = getBomAryProduct();
        for(BomAryProduct bomAryProduct : bomAryProductList) {
            if(bomAryProduct.isVerify()) continue;
            String product = bomAryProduct.getProduct();
            List<String> cellMtrlList = getCellMtrl(product);
            List<String> tftMtrlList = restService.getTftMatrialByCellMtrlFroAry(cellMtrlList);
            if(tftMtrlList==null || tftMtrlList.size()==0) continue;
            List<Map> mapList = restService.getAryMatrialByTftMtrl(tftMtrlList);
            if(mapList== null || mapList.size()==0) continue;

            List<BomAry> bomAryList = new ArrayList<>();
            for(Map map : mapList) {
                BomAry bomAry = new BomAry();
                String material = (String) map.get("material");
                bomAry.setId(product + "_" + material);
                bomAry.setProduct(product);
                bomAry.setMaterial(material);
                Double baseQty = ((Integer) map.get("baseQty")).doubleValue();
                Double qty = (Double) map.get("qty");
                bomAry.setUsageQty(DoubleUtil.divide(qty, baseQty));
                bomAry.setMeasureUnit((String) map.get("measureUnit"));
                String materialGroup = materialService.getMaterialGroup(material);
                bomAry.setMaterialName(materialService.getMaterialName(material));
                bomAry.setMaterialGroup(materialGroup);
                bomAry.setMaterialGroupName(materialGroupService.getMaterialGroupName(materialGroup));
                bomAryList.add(bomAry);
            }

            bomAryRepository.saveAll(bomAryList);
            bomAryProduct.setVerify(true);
            bomAryProductRepository.save(bomAryProduct);
        }
    }

    @Override
    public List<String> getCellMtrl(String product) {
        return aryMpsModeRepository.getCellMtrlByProduct(product);
    }

    @Override
    public List<BomAry> getBomAry(String product) {
        return bomAryRepository.findByProduct(product);
    }

    @Override
    public Page<BomAryProduct> queryProduct(int page, int limit, String searchProduct) {
        Pageable pageable = PageRequest.of(page, limit,  Sort.Direction.ASC, "product");
        return bomAryProductRepository.findByProductLike(searchProduct+"%", pageable);
    }

    @Override
    public List<BomAryProduct> getBomAryProduct() {
        return bomAryProductRepository.findAll();
    }

    @Override
    public BomAryProduct getBomAryProduct(String product) {
        return bomAryProductRepository.findById(product).orElse(null);
    }

    @Override
    public List<Map> getBomAryOc(String product) {
        if(StringUtils.contains(product, " ")) {
            product = product.substring(0, product.indexOf(" "));
        }
        List<Map> mapList = cellMaterialService.getOcMaterial(product);
        List<Map> list = new ArrayList<>();
        if(mapList==null || mapList.size()==0) return list;
        list.add(mapList.get(0));
        return list;
    }
}
