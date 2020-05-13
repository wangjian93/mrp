package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wj
 * @version 1.0
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
