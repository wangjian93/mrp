package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.MpsVer;
import com.ivo.mrp2.repository.MpsVerRepository;
import com.ivo.mrp2.service.MpsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wj
 * @version 1.0
 */
 @Service
 @Slf4j
public class MpsServiceImpl implements MpsService {

     private MpsVerRepository mpsVerRepository;

     @Autowired
    public MpsServiceImpl(MpsVerRepository mpsVerRepository) {
        this.mpsVerRepository = mpsVerRepository;
    }

    @Override
    public void syncMps() {

    }

    @Override
    public void syncMpsLcm() {

    }

    @Override
    public void syncMpsCell() {

    }

    @Override
    public void syncMpsAry() {

    }

    @Override
    public MpsVer getMpsVer(String ver) {
        return mpsVerRepository.findById(ver).orElse(null);
    }
}
