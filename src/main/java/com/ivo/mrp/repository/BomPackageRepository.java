package com.ivo.mrp.repository;

import com.ivo.mrp.entity.packaging.BomPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface BomPackageRepository extends JpaRepository<BomPackage, Long> {

    /**
     * 筛选机种、类型、连片数、抽单模式
     * @param product 机种
     * @param Type 单片/连片类型
     * @param linkQty 连片数
     * @param mode 抽单模式
     * @return BomPackage
     */
    BomPackage findFirstByProductAndTypeAndLinkQtyAndMode(String product, String Type, Double linkQty, String mode);

    /**
     * 筛选机种
     * @param product 机种
     * @return List<BomPackage>
     */
    List<BomPackage> findByProduct(String product);

    /**
     * 统计机种
     * @return List<String>
     */
    @Query(value = "SELECT DISTINCT product from mrp3_bom_package ORDER BY product", nativeQuery = true)
    List<String> getPackageProduct();

    /**
     * 分页查询
     * @param searchProduct 机种
     * @param pageable 分页
     * @return Page<BomPackage>
     */
    Page<BomPackage> findByProductLike(String searchProduct, Pageable pageable);
}
