package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface ProductRepository extends JpaRepository<Product, String> {

    /**
     * 查询所有的机种
     * @return List<String>
     */
    @Query(value = "select product from mrp2_product p where p.product like :productSearch limit :limit", nativeQuery = true)
    List<String> searchProduct(@Param("productSearch") String productSearch, @Param("limit") int limit);

}
