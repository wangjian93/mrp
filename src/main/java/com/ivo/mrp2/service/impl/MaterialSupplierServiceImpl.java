package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.MaterialSupplier;
import com.ivo.mrp2.repository.MaterialSupplierRepository;
import com.ivo.mrp2.service.MaterialSupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class MaterialSupplierServiceImpl implements MaterialSupplierService {

    private MaterialSupplierRepository materialSupplierRepository;

    @Autowired
    public MaterialSupplierServiceImpl(MaterialSupplierRepository materialSupplierRepository) {
        this.materialSupplierRepository = materialSupplierRepository;
    }

    @Override
    public Page<MaterialSupplier> getPageMaterialSupplier(int page, int limit, String material, String supplierCode) {
        Pageable pageable = PageRequest.of(page-1, limit);
        material += "%";
        supplierCode += "%";
        return materialSupplierRepository.findByMaterialLikeAndSupplierCodeLike(material, supplierCode, pageable);
    }
}
