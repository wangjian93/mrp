package com.ivo.mrp.service.packageing;

import com.ivo.mrp.entity.DpsVer;
import com.ivo.mrp.entity.packaging.BomPackage;
import com.ivo.mrp.entity.packaging.DpsCellProduct;
import com.ivo.mrp.entity.packaging.DpsPackage;
import com.ivo.mrp.repository.packaging.DpsCellProductRepository;
import com.ivo.mrp.repository.packaging.DpsPackageRepository;
import com.ivo.mrp.service.DpsService;
import com.ivo.rest.RestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class DpsPackageServiceImpl implements DpsPackageService {

    private RestService restService;

    private DpsService dpsService;

    private BomPackageService bomPackageService;

    private DpsCellProductRepository dpsCellProductRepository;

    private DpsPackageRepository dpsPackageRepository;

    @Autowired
    public DpsPackageServiceImpl(RestService restService, DpsService dpsService, BomPackageService bomPackageService,
                                 DpsCellProductRepository dpsCellProductRepository, DpsPackageRepository dpsPackageRepository) {
        this.restService = restService;
        this.dpsService = dpsService;
        this.bomPackageService = bomPackageService;
        this.dpsCellProductRepository = dpsCellProductRepository;
        this.dpsPackageRepository = dpsPackageRepository;
    }

    @Override
    public void syncDpsPackage() {
        log.info("同步CELL包材DPS >> START");
        List<String> verList = restService.getDpsCellAryVer();
        if(verList == null || verList.size()==0) return;
        for(String ver : verList) {
            List list = dpsService.getDpsVerByFileVer(ver, DpsVer.Type_Package);
            if(list == null || list.size() == 0) {
                syncDpsPackage(ver);
            }
        }
        log.info("同步CELL包材DPS >> END");
    }

    @Override
    public void syncDpsPackage(String ver) {
        log.info("同步CELL包材DPS版本" + ver);
        List<String> productList = bomPackageService.getPackageProduct();
        List<Map> mapList = restService.getDpsPackage(ver, productList);
        if(mapList==null || mapList.size() == 0) return;
        String fab = "CELL";
        String dps_ver = dpsService.generateDpsVer();
        DpsVer dpsVer = new DpsVer();
        dpsVer.setFab(fab);
        dpsVer.setDpsFile(ver);
        dpsVer.setVer(dps_ver);
        dpsVer.setSource(DpsVer.Source_Cell);
        dpsVer.setType(DpsVer.Type_Package);
        dpsVer.setCreator("SYS");
        Date startDate = null;
        Date endDate = null;
        String file_name = "";

        List<DpsPackage> dpsPackageList = new ArrayList<>();
        List<DpsCellProduct> dpsCellProductList = new ArrayList<>();
        for(Map map : mapList) {
            file_name = (String) map.get("file_name");
            String product = (String) map.get("model_id_dps");
            double demandQty = (Double) map.get("qty");
            Date fabDate = (Date) map.get("fab_date");

            DpsCellProduct dpsCellProduct = new DpsCellProduct();
            dpsCellProduct.setVer(dps_ver);
            dpsCellProduct.setProduct(product);
            dpsCellProduct.setFab(fab);
            dpsCellProduct.setFabDate(fabDate);
            dpsCellProduct.setDemandQty(demandQty);
            dpsCellProductList.add(dpsCellProduct);

            List<BomPackage> bomPackageList = bomPackageService.getBomPackage(product);
            //单片、连片各取第一个
            List<BomPackage> newBomPackageList = new ArrayList<>();
            boolean d_flag = true;
            boolean l_flag = true;
            for(BomPackage bomPackage : bomPackageList) {
                if(bomPackage.getType().equals(BomPackage.TYPE_D)) {
                    if(d_flag) {
                        newBomPackageList.add(bomPackage);
                        d_flag = false;
                    }
                } else {
                    if(l_flag) {
                        newBomPackageList.add(bomPackage);
                        l_flag = false;
                    }
                }
            }
            for(BomPackage bomPackage : newBomPackageList) {
                DpsPackage dpsPackage = new DpsPackage();
                dpsPackage.setFab(fab);
                dpsPackage.setVer(dps_ver);
                dpsPackage.setPackageId(bomPackage.getId());
                dpsPackage.setProduct(product);
                dpsPackage.setType(bomPackage.getType());
                dpsPackage.setLinkQty(bomPackage.getLinkQty());
                dpsPackage.setFabDate(fabDate);
                dpsPackage.setDemandQty(demandQty);
                dpsPackageList.add(dpsPackage);
            }

            //日期区间
            if(startDate == null || startDate.after(fabDate)) {
                startDate =fabDate;
            }
            if(endDate == null || endDate.before(fabDate)) {
                endDate = fabDate;
            }
        }

        dpsVer.setStartDate(startDate);
        dpsVer.setEndDate(endDate);
        dpsVer.setFileName(file_name);
        dpsService.saveDpsVer(dpsVer);
        dpsCellProductRepository.saveAll(dpsCellProductList);
        dpsPackageRepository.saveAll(dpsPackageList);
    }

    @Override
    public List<String> getPackageId(List<String> dpsVers) {
        return dpsPackageRepository.getPackageId(dpsVers);
    }

    @Override
    public List<DpsPackage> getDpsPackage(List<String> dpsVers, String packageId) {
        return dpsPackageRepository.findByVerInAndPackageId(dpsVers, packageId);
    }

    @Override
    public List<DpsPackage> getDpsPackage(String dpsVer, String packageId) {
        return dpsPackageRepository.findByVerAndPackageId(dpsVer, packageId);
    }

    @Override
    public Page<Map> getPagePackageId(String ver, int page, int limit, String searchProduct) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "packageId");
        return dpsPackageRepository.getPagePackageId(ver, searchProduct+"%", pageable);
    }

    @Override
    public List<DpsCellProduct> getDpsCellProduct(String ver, String product) {
        return dpsCellProductRepository.findByVerAndProduct(ver, product);
    }

    @Override
    public List<DpsPackage> getDpsPackageByProduct(String ver, String product) {
        return dpsPackageRepository.findByVerAndProduct(ver, product);
    }

    @Override
    public void delete(List<DpsPackage> dpsPackageList) {
        dpsPackageRepository.deleteAll(dpsPackageList);
    }

    @Override
    public void save(List<DpsPackage> dpsPackageList) {
        dpsPackageRepository.saveAll(dpsPackageList);
    }
}
