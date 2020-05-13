//package com.ivo.mrp.service.impl;
//
//import com.ivo.mrp.entity.MaterialScrapRate;
//import com.ivo.mrp.repository.MaterialScrapRateRepository;
//import com.ivo.mrp.service.MaterialScrapRateService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * @author wj
// * @version 1.0
// */
//@Service
//public class MaterialScrapRateServiceImpl implements MaterialScrapRateService {
//
//    private MaterialScrapRateRepository scrapRateRepository;
//
//    @Autowired
//    public MaterialScrapRateServiceImpl(MaterialScrapRateRepository scrapRateRepository) {
//        this.scrapRateRepository = scrapRateRepository;
//    }
//
//    @Override
//    public List<MaterialScrapRate> getMaterialScrapRate() {
//        return scrapRateRepository.findAll();
//    }
//}
