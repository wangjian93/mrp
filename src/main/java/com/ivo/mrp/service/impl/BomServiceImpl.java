package com.ivo.mrp.service.impl;

import com.ivo.common.BatchService;
import com.ivo.mrp.entity.direct.lcm.BomLcm;
import com.ivo.mrp.entity.lcmPackaging.BomPackagingLcm;
import com.ivo.mrp.repository.*;
import com.ivo.mrp.service.BomService;
import com.ivo.rest.RestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class BomServiceImpl implements BomService {



    private RestService restService;

    private BomLcmRepository bomLcmRepository;

    private BatchService batchService;

    private BomPackagingLcmRepository bomPackagingLcmRepository;

    @Autowired
    public BomServiceImpl(RestService restService,
                          BomLcmRepository bomLcmRepository,
                          BatchService batchService,
                          BomPackagingLcmRepository bomPackagingLcmRepository) {
        this.restService = restService;
        this.bomLcmRepository = bomLcmRepository;
        this.batchService = batchService;
        this.bomPackagingLcmRepository = bomPackagingLcmRepository;
    }

    @Override
    public void syncBomLcm() {
        log.info("同步LCM的BOM >> START");
        bomLcmRepository.truncateTable();
        bomPackagingLcmRepository.truncateTable();
        syncBomLcm1();
        syncBomLcm2();
        log.info("同步LCM的BOM >> END");
    }

    private void syncBomLcm1() {
        log.info("同步LCM1的BOM >> START");
        List<Map> mapList = restService.getBomLcm1();
        if(mapList == null || mapList.size() == 0) return;

        List<BomLcm> bomLcmList = new ArrayList<>();
        List<BomPackagingLcm> bomPackagingLcmList = new ArrayList<>();
        for(Map map : mapList) {
            String material = (String) map.get("material");
            String materialGroup = (String) map.get("materialGroup");

            //分开主材包材
            if(StringUtils.equalsAny(materialGroup, "906", "912", "916", "917", "918","919","920","921","922", "305") ||
                    (materialGroup.equals("924") && StringUtils.equalsAny(material, "3822617-010","380C41Q-010",
                            "2846802.000","2694001.000","32A93A6-000", "3840302.000","3820304.000","3820153.000","3820155.000",
                            "39033S0-000","3280103-000","3280105-000","3280101-000","3280102-000","3280104-000","3280002-000",
                            "3280001-000","3802901-000","3820303.000"))
            ) {
                BomPackagingLcm bomLcm = new BomPackagingLcm();
                bomLcm.setFab("LCM1");
                bomLcm.setProduct((String) map.get("product"));
                bomLcm.setMaterial(material);
                bomLcm.setMeasureUnit((String) map.get("measureUnit"));
                bomLcm.setMaterialName((String) map.get("materialName"));
                bomLcm.setMaterialGroup(materialGroup);
                bomLcm.setMaterialGroupName((String) map.get("materialGroupName"));
                bomLcm.setUsageQty(((BigDecimal) map.get("usageQty")).doubleValue());

                bomPackagingLcmList.add(bomLcm);
            } else {
                BomLcm bomLcm = new BomLcm();
                bomLcm.setFab("LCM1");
                bomLcm.setProduct((String) map.get("product"));
                bomLcm.setMaterial(material);
                bomLcm.setMeasureUnit((String) map.get("measureUnit"));
                bomLcm.setMaterialName((String) map.get("materialName"));
                bomLcm.setMaterialGroup(materialGroup);
                bomLcm.setMaterialGroupName((String) map.get("materialGroupName"));
                bomLcm.setUsageQty(((BigDecimal) map.get("usageQty")).doubleValue());

                bomLcmList.add(bomLcm);
            }
        }
        batchService.batchInsert(bomLcmList);
        batchService.batchInsert(bomPackagingLcmList);
        log.info("同步LCM1的BOM >> END");
    }

    private void syncBomLcm2() {
        log.info("同步LCM2的BOM >> START");
        List<Map> mapList = restService.getBomLcm2();
        if(mapList == null || mapList.size() == 0) return;

        List<BomLcm> bomLcmList = new ArrayList<>();
        List<BomPackagingLcm> bomPackagingLcmList = new ArrayList<>();
        for(Map map : mapList) {
            String material = (String) map.get("material");
            String materialGroup = (String) map.get("materialGroup");

            //分开主材包材
            if(StringUtils.equalsAny(materialGroup, "906", "912", "916", "917", "918","919","920","921","922", "305") ||
                    (materialGroup.equals("924") && StringUtils.equalsAny(material, "2843801-000","2843802-000","28461L0-010","28468K0-000","32A93A6-000",
                            "39033S0-000","3802901-000","3280101-000","3280102-000"))
            ) {
                BomPackagingLcm bomLcm = new BomPackagingLcm();
                bomLcm.setFab("LCM2");
                bomLcm.setProduct((String) map.get("product"));
                bomLcm.setMaterial(material);
                bomLcm.setMeasureUnit((String) map.get("measureUnit"));
                bomLcm.setMaterialName((String) map.get("materialName"));
                bomLcm.setMaterialGroup(materialGroup);
                bomLcm.setMaterialGroupName((String) map.get("materialGroupName"));
                bomLcm.setUsageQty(((BigDecimal) map.get("usageQty")).doubleValue());

                bomPackagingLcmList.add(bomLcm);
            } else {
                BomLcm bomLcm = new BomLcm();
                bomLcm.setFab("LCM2");
                bomLcm.setProduct((String) map.get("product"));
                bomLcm.setMaterial(material);
                bomLcm.setMeasureUnit((String) map.get("measureUnit"));
                bomLcm.setMaterialName((String) map.get("materialName"));
                bomLcm.setMaterialGroup(materialGroup);
                bomLcm.setMaterialGroupName((String) map.get("materialGroupName"));
                bomLcm.setUsageQty(((BigDecimal) map.get("usageQty")).doubleValue());

                bomLcmList.add(bomLcm);
            }
        }
        batchService.batchInsert(bomLcmList);
        batchService.batchInsert(bomPackagingLcmList);
        log.info("同步LCM2的BOM >> END");
    }

    @Override
    public List<BomLcm> getLcmBom(String product, String fab) {
        return bomLcmRepository.findByFabAndProduct(fab, product);
    }

    @Override
    public Page<Map> queryProduct(int page, int limit, String fab, String searchProduct) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "product");
        if(StringUtils.equalsAnyIgnoreCase(fab, "LCM1", "LCM2")) {
            return bomLcmRepository.queryProduct(fab, searchProduct+"%", pageable);
        }
        return null;
    }
}
