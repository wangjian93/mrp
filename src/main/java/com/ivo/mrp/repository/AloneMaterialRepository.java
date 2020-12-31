package com.ivo.mrp.repository;

import com.ivo.mrp.entity.lcmPackaging.AloneMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface AloneMaterialRepository extends JpaRepository<AloneMaterial, Long> {

    List<AloneMaterial> findByFab(String fab);
}
