package com.ivo.mrp.service.impl;

import com.ivo.mrp.entity.lcmPackaging.AloneMaterial;
import com.ivo.mrp.entity.lcmPackaging.BomPackagingLcm;
import com.ivo.mrp.repository.AloneMaterialRepository;
import com.ivo.mrp.repository.BomPackagingLcmRepository;
import com.ivo.mrp.service.BomPackageLcmService;
import com.ivo.rest.RestService;
import lombok.extern.slf4j.Slf4j;
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
@Service
@Slf4j
public class BomPackageLcmServiceImpl implements BomPackageLcmService {

    private BomPackagingLcmRepository bomPackagingLcmRepository;

    private RestService restService;

    private AloneMaterialRepository aloneMaterialRepository;

    @Autowired
    public BomPackageLcmServiceImpl(BomPackagingLcmRepository bomPackagingLcmRepository,
                                    RestService restService, AloneMaterialRepository aloneMaterialRepository) {
        this.bomPackagingLcmRepository = bomPackagingLcmRepository;
        this.restService = restService;
        this.aloneMaterialRepository = aloneMaterialRepository;
    }

    @Override
    public Page<Map> queryProduct(int page, int limit, String fab, String searchProduct) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "product");
        return bomPackagingLcmRepository.queryProduct(fab, searchProduct+"%", pageable);
    }

    @Override
    public List<BomPackagingLcm> getLcmPackageBom(String fab, String product) {
        return bomPackagingLcmRepository.findByFabAndProduct(fab, product);
    }

    @Override
    public void syncLcmPackageBom() {
        syncLcm2PackageBom();
        syncLcm1PackageBom();
    }

    private  void syncLcm1PackageBom() {
        log.info("LCM1 包材 BOM...");
        List<Map> mapList = restService.getBomPackageLcm1();
        if(mapList == null || mapList.size() == 0) return;
        int i = mapList.size();
        List<BomPackagingLcm> bomPackagingLcmList = new ArrayList<>();
        for(Map map : mapList) {
            System.out.println("剩余" + i--);
            BomPackagingLcm bomLcm = new BomPackagingLcm();
            bomLcm.setFab("LCM1");
            bomLcm.setProduct((String) map.get("product"));
            bomLcm.setMaterial((String) map.get("material"));
            bomLcm.setMeasureUnit((String) map.get("measureUnit"));
            bomLcm.setMaterialName((String) map.get("materialName"));
            bomLcm.setMaterialGroup((String) map.get("materialGroup"));
            bomLcm.setMaterialGroupName((String) map.get("materialGroupName"));
            bomLcm.setUsageQty(((BigDecimal) map.get("usageQty")).doubleValue());
            bomPackagingLcmList.add(bomLcm);
        }
        bomPackagingLcmRepository.saveAll(bomPackagingLcmList);
    }

    private  void syncLcm2PackageBom() {
        log.info("LCM2 包材 BOM...");
        List<Map> mapList = restService.getBomPackageLcm2();
        if(mapList == null || mapList.size() == 0) return;
        int i = mapList.size();
        List<BomPackagingLcm> bomPackagingLcmList = new ArrayList<>();
        for(Map map : mapList) {
            System.out.println("剩余" + i--);
            BomPackagingLcm bomLcm = new BomPackagingLcm();
            bomLcm.setFab("LCM2");
            bomLcm.setProduct((String) map.get("product"));
            bomLcm.setMaterial((String) map.get("material"));
            bomLcm.setMeasureUnit((String) map.get("measureUnit"));
            bomLcm.setMaterialName((String) map.get("materialName"));
            bomLcm.setMaterialGroup((String) map.get("materialGroup"));
            bomLcm.setMaterialGroupName((String) map.get("materialGroupName"));
            bomLcm.setUsageQty(((BigDecimal) map.get("usageQty")).doubleValue());
            bomPackagingLcmList.add(bomLcm);
        }
        bomPackagingLcmRepository.saveAll(bomPackagingLcmList);
    }

    @Override
    public List<AloneMaterial> getAloneMaterial() {
        return aloneMaterialRepository.findAll();
    }
}
