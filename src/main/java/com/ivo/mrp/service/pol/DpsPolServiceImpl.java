package com.ivo.mrp.service.pol;

import com.ivo.mrp.entity.DpsVer;
import com.ivo.mrp.entity.packaging.BomPackage;
import com.ivo.mrp.entity.packaging.DpsCellProduct;
import com.ivo.mrp.entity.packaging.DpsPackage;
import com.ivo.mrp.entity.pol.DpsCellPolProduct;
import com.ivo.mrp.entity.pol.DpsPol;
import com.ivo.mrp.repository.pol.DpsCellPolProductRepository;
import com.ivo.mrp.repository.pol.DpsPolRepository;
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
@Service
@Slf4j
public class DpsPolServiceImpl implements DpsPolService {

    private RestService restService;

    private DpsService dpsService;

    private BomPolService bomPolService;

    private DpsCellPolProductRepository dpsCellPolProductRepository;

    private DpsPolRepository dpsPolRepository;

    @Autowired
    public DpsPolServiceImpl(RestService restService, DpsService dpsService, BomPolService bomPolService,
                             DpsCellPolProductRepository dpsCellPolProductRepository, DpsPolRepository dpsPolRepository) {
        this.restService = restService;
        this.dpsService = dpsService;
        this.bomPolService = bomPolService;
        this.dpsCellPolProductRepository = dpsCellPolProductRepository;
        this.dpsPolRepository = dpsPolRepository;
    }

    @Override
    public void syncDpsPol() {
        log.info("同步CELL POL DPS >> START");
        List<String> verList = restService.getDpsCellAryVer();
        if(verList == null || verList.size()==0) return;
        for(String ver : verList) {
            List list = dpsService.getDpsVerByFileVer(ver, DpsVer.Type_Pol);
            if(list == null || list.size() == 0) {
                syncDpsPol(ver);
            }
        }
        log.info("同步CELL POL DPS >> END");
    }

    @Override
    public void syncDpsPol(String ver) {
        log.info("同步CELL POL DPS版本" + ver);
        List<Map> mapList = restService.getDpsPol(ver);
        if(mapList==null || mapList.size() == 0) return;
        String fab = "CELL";
        String dps_ver = dpsService.generateDpsVer();
        DpsVer dpsVer = new DpsVer();
        dpsVer.setFab(fab);
        dpsVer.setDpsFile(ver);
        dpsVer.setVer(dps_ver);
        dpsVer.setSource(DpsVer.Source_Cell);
        dpsVer.setType(DpsVer.Type_Pol);
        dpsVer.setCreator("SYS");
        Date startDate = null;
        Date endDate = null;
        String file_name = "";

        List<DpsPol> dpsPolList = new ArrayList<>();
        List<DpsCellPolProduct> dpsCellProductList = new ArrayList<>();
        for(Map map : mapList) {
            file_name = (String) map.get("file_name");
            String project = (String) map.get("model_id_dps");
            double demandQty = (Double) map.get("qty");
            Date fabDate = (Date) map.get("fab_date");

            DpsCellPolProduct dpsCellPolProduct = new DpsCellPolProduct();
            dpsCellPolProduct.setVer(dps_ver);
            dpsCellPolProduct.setProject(project);
            dpsCellPolProduct.setFab(fab);
            dpsCellPolProduct.setFabDate(fabDate);
            dpsCellPolProduct.setDemandQty(demandQty);
            dpsCellProductList.add(dpsCellPolProduct);

            List<String> productList = bomPolService.getBomPolProduct(project);
            if(productList != null && productList.size()==0) continue;
            String product = productList.get(0);
            DpsPol dpsPol = new DpsPol();
            dpsPol.setVer(dps_ver);
            dpsPol.setProduct(product);
            dpsPol.setProject(project);
            dpsPol.setFabDate(fabDate);
            dpsPol.setDemandQty(demandQty);
            dpsPolList.add(dpsPol);

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
        dpsCellPolProductRepository.saveAll(dpsCellProductList);
        dpsPolRepository.saveAll(dpsPolList);
    }

    @Override
    public Page<Map> getPageProduct(String ver, int page, int limit, String searchProduct) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "product");
        return dpsPolRepository.getPageProduct(ver, searchProduct+"%", pageable);
    }

    @Override
    public List<DpsPol> getDpsPol(String ver, String product) {
        return dpsPolRepository.findByVerAndProduct(ver, product);
    }

    @Override
    public List<DpsCellPolProduct> getDpsCellPolProduct(String ver, String project) {
        return dpsCellPolProductRepository.findByVerAndProject(ver, project);
    }

    @Override
    public void delete(List<DpsPol> list) {
        dpsPolRepository.deleteAll(list);
    }

    @Override
    public void save(List<DpsPol> list) {
        dpsPolRepository.saveAll(list);
    }

    @Override
    public List<DpsPol> getDpsPolByProject(String ver, String project) {
        return dpsPolRepository.findByVerAndProject(ver, project);
    }

    @Override
    public List<String> getDpsPolProduct(String ver) {
        return dpsPolRepository.getDpsPolProduct(ver);
    }
}
