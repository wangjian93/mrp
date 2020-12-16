package com.ivo.mrp.repository;

import com.ivo.mrp.entity.ProductCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface ProductCustomerRepository extends JpaRepository<ProductCustomer, Long> {

    @Query(value = "select distinct p.customer from ProductCustomer p where p.product=:product")
    List<String> getCustomer(@Param("product") String product);

    @Query(value = "select distinct p.customer from ProductCustomer p where p.product in :products")
    List<String> getCustomer(@Param("products") List<String> products);
}
