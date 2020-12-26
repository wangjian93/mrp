package com.ivo.mrp.repository.packaging;

import com.ivo.mrp.entity.packaging.PackageSupplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface

PackageSupplierRepository extends JpaRepository<PackageSupplier, Long> {

    List<PackageSupplier> findByMonthAndProductLikeAndTypeLikeAndLinkQtyLikeAndMaterialType(String month, String product, String type, String linkQty, String materialType);

    @Query(value = "select DISTINCT material_type as materialType, supplier_code as supplierCode from mrp3_package_supplier where month ='2020-12'", nativeQuery = true)
    List<Map> getSupplier(@Param("month") String month);

    List<PackageSupplier> findByMonthAndProductLike(String month, String product);

}
