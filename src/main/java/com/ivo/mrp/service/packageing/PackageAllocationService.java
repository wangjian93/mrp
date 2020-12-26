package com.ivo.mrp.service.packageing;

import com.ivo.mrp.entity.packaging.PackageAllocation;

import java.sql.Date;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface PackageAllocationService {

    List<PackageAllocation> getPackageAllocation(String packageId, String material, Date fabDate);


    void save(List<PackageAllocation> packageAllocationList);
}
