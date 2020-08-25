package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.ProductCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface ProductCustomerRepository extends JpaRepository<ProductCustomer, Long> {

    @Query(value = "select p.customer from ProductCustomer p where p.product=:product")
    List<String> getCustomer(String product);

    @Query(value = "select p.customer from ProductCustomer p where p.product in :products")
    List<String> getCustomer(List<String> products);
}
