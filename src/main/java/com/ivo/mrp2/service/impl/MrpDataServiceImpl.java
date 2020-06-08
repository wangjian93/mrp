package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.MrpData;
import com.ivo.mrp2.repository.MrpDataRepository;
import com.ivo.mrp2.service.MrpDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author wj
 * @version 1.0
 */
 @Service
public class MrpDataServiceImpl implements MrpDataService {

     private MrpDataRepository mrpDataRepository;

     @Autowired
    public MrpDataServiceImpl(MrpDataRepository mrpDataRepository) {
        this.mrpDataRepository = mrpDataRepository;
    }

    @Override
    public List<MrpData> getShortMrpData(String mrpVer) {
        return mrpDataRepository.findByMrpVerAndShortQtyLessThan(mrpVer,0);
    }
}
