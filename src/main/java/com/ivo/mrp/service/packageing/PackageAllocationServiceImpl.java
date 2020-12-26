package com.ivo.mrp.service.packageing;

import com.ivo.mrp.entity.packaging.PackageAllocation;
import com.ivo.mrp.repository.packaging.PackageAllocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Service
public class PackageAllocationServiceImpl implements PackageAllocationService {

    private PackageAllocationRepository packageAllocationRepository;

    @Autowired
    public PackageAllocationServiceImpl(PackageAllocationRepository packageAllocationRepository) {
        this.packageAllocationRepository = packageAllocationRepository;
    }

    @Override
    public List<PackageAllocation> getPackageAllocation(String packageId, String material, Date fabDate) {
        return packageAllocationRepository.findByPackageIdAndMaterialAndFabDate(packageId, material, fabDate);
    }

    @Override
    public void save(List<PackageAllocation> packageAllocationList) {
        packageAllocationRepository.saveAll(packageAllocationList);
    }
}
