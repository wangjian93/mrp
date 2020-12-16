package com.ivo.mrp.service.impl;


import com.ivo.mrp.repository.ProductCustomerRepository;
import com.ivo.mrp.service.ProductCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Service
@Slf4j
public class ProductCustomerServiceImpl implements ProductCustomerService {

    private ProductCustomerRepository productCustomerRepository;

    @Autowired
    public ProductCustomerServiceImpl(ProductCustomerRepository productCustomerRepository) {
        this.productCustomerRepository = productCustomerRepository;
    }

    @Override
    public List<String> getCustomer(String product) {
        return productCustomerRepository.getCustomer(product);
    }

    @Override
    public List<String> getCustomer(List<String> products) {
        return productCustomerRepository.getCustomer(products);
    }
}
