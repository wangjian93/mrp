package com.ivo.rest.dps.service.impl;

import com.ivo.mrp2.entity.Dps;
import com.ivo.rest.dps.entity.RestDps;
import com.ivo.rest.dps.mapper.DpsMapper;
import com.ivo.rest.dps.service.RestDpsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class RestDpsServiceImpl implements RestDpsService {

    private DpsMapper dpsMapper;

    @Autowired
    public RestDpsServiceImpl(DpsMapper dpsMapper) {
        this.dpsMapper = dpsMapper;
    }

    @Override
    public List<RestDps> getDpsByVer(String ver) {
        log.info("获取DPS,版本:" + ver);
        return dpsMapper.getDpsByVer(ver);
    }

    @Override
    public List<Dps> getDpsByVer2(String ver) {
        List<Dps> dpsList = new ArrayList<>();
        List<RestDps> restDpsList = getDpsByVer(ver);
        for(RestDps restDps : restDpsList) {
            Dps dps = new Dps();
            dps.setProduct(restDps.getProd_id());
            dps.setModel(restDps.getModel_id());
            dps.setFab(restDps.getFab_id());
            dps.setFabDate(restDps.getFab_date());
            dps.setQty(restDps.getBpc_qty());
            dps.setDpsVer(restDps.getDps_ver());
            dpsList.add(dps);
        }
        return dpsList;
    }

    @Override
    public List<String> getDpsVer() {
        log.info("获取DPS所有版本");
        return dpsMapper.getDpsVer();
    }
}
