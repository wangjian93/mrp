package com.ivo.mrp.service.packageing;

import com.ivo.mrp.entity.packaging.PackageSupplier;

import java.io.InputStream;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface PackageSupplierService {

    List<PackageSupplier> getPackageSupplier(String month, String product, String type, String linkQty,  String materialType);

    void importExcel(InputStream inputStream, String fileName);
}
