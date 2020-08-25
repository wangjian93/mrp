package com.ivo.mrp.repository;

import com.ivo.mrp.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wj
 * @version 1.0
 */
public interface MaterialRepository extends JpaRepository<Material, String> {
}
