package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.MaterialSupplier;
import com.ivo.mrp2.entity.Supplier;
import com.ivo.mrp2.repository.MaterialSupplierRepository;
import com.ivo.mrp2.service.MaterialSupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public Page<Map> getPageMaterialSupplier(int page, int limit, String material, String supplier) {
        Sort sort = Sort.by(Sort.Direction.ASC, "material");
        Pageable pageable = PageRequest.of(page, limit, sort);
        return materialSupplierRepository.getPageMaterialSupplier(material+"%", supplier+"%", pageable);
    }

    @Override
    public List<Supplier> getMaterialSupplier(String material) {
        return materialSupplierRepository.getSupplierByMaterial(material);
    }

    @Override
    public void add(String material, String supplierCode) {
        log.info("添加材料与供应商信息>> START");
        if(getMaterialSupplier(material, supplierCode) == null) {
            MaterialSupplier materialSupplier = new MaterialSupplier();
            materialSupplier.setMaterial(material);
            materialSupplier.setSupplierCode(supplierCode);
            materialSupplierRepository.save(materialSupplier);
        }
        log.info("添加材料与供应商信息>> START");
    }

    @Override
    public void delete(String material, String supplierCode) {
        log.info("删除材料与供应商信息>> START");
        MaterialSupplier materialSupplier = getMaterialSupplier(material, supplierCode);
        if(materialSupplier != null) {
            materialSupplierRepository.delete(materialSupplier);
        }
        log.info("删除材料与供应商信息>> START");
    }

    private MaterialSupplier getMaterialSupplier(String material, String supplierCode) {
        return materialSupplierRepository.findFirstByMaterialAndSupplierCode(material, supplierCode);
    }

}
