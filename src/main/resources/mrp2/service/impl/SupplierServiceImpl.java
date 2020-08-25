package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.Supplier;
import com.ivo.mrp2.repository.SupplierRepository;
import com.ivo.mrp2.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class SupplierServiceImpl implements SupplierService {

    private SupplierRepository supplierRepository;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public void updateSName(String id, String sName) {
        log.info("修改供应商简称>> START");
        Supplier supplier = getSupplier(id);
        if(supplier == null) return;
        supplier.setSName(sName);
        supplierRepository.save(supplier);
        log.info("修改供应商简称>> END");
    }

    @Override
    public Supplier getSupplier(String supplierCode) {
        return supplierRepository.findById(supplierCode).orElse(null);
    }


    @Override
    public Page<Supplier> searchSupplier(String supplierSearch, int limit) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(0, limit, sort);
        return supplierRepository.findByNameLike(supplierSearch+"%", pageable);
    }

    @Override
    public Page<Supplier> getPageSupplier(int page, int limit, String supplier) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, limit, sort);
        return supplierRepository.findByNameLike(supplier+"%", pageable);
    }
}
