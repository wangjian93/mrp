package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.Bom;
import com.ivo.mrp2.entity.Product;
import com.ivo.mrp2.repository.ProductRepository;
import com.ivo.mrp2.service.BomService;
import com.ivo.mrp2.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wj
 * @version 1.0
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    private BomService bomService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, BomService bomService) {
        this.productRepository = productRepository;
        this.bomService = bomService;
    }

    @Override
    public void syncProduct() {
        log.info("同步机种信息>> START");
        List<Bom> bomList = bomService.getAllBom();
        List<Product> productList = new ArrayList<>();
        Set<String> productSet = new HashSet<>();
        for(Bom bom : bomList) {
            if(!productSet.contains(bom.getProduct())) {
                Product product = new Product();
                product.setProduct(bom.getProduct());
                product.setMemo("来自BOM同步");
                productSet.add(bom.getProduct());
                productList.add(product);
            }
        }
        productRepository.saveAll(productList);
        log.info("同步机种信息>> END");
    }

    @Override
    public List<String> searchProduct(String productSearch, int  limit) {
        return productRepository.searchProduct(productSearch + "%", limit);
    }
}
