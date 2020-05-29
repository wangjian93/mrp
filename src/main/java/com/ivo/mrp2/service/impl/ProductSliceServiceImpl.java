package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.ProductSlice;
import com.ivo.mrp2.repository.ProductSliceRepository;
import com.ivo.mrp2.service.ProductSliceService;
import com.ivo.rest.eif.service.EifSliceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class ProductSliceServiceImpl implements ProductSliceService {

    private ProductSliceRepository productSliceRepository;

    private EifSliceService eifSliceService;

    @Autowired
    public ProductSliceServiceImpl(ProductSliceRepository productSliceRepository, EifSliceService eifSliceService) {
        this.productSliceRepository = productSliceRepository;
        this.eifSliceService = eifSliceService;
    }

    @Override
    public double getProductSliceService(String product) {
        ProductSlice p = productSliceRepository.findById(product).orElse(null);
        if(p == null) return 0;
        return p.getSlice();
    }

    @Override
    public void syncProductSlice() {
        log.info(">> 同步机种的切片数");
        productSliceRepository.deleteAll();
        productSliceRepository.saveAll(eifSliceService.getProductSlice());
        log.info(">> END");
    }
}
