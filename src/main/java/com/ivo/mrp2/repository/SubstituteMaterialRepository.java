package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.SubstituteMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface SubstituteMaterialRepository extends JpaRepository<SubstituteMaterial, Long> {

    List<SubstituteMaterial> findBySubstituteGroup(int group);

    SubstituteMaterial findByPlantAndProductAndMaterialAndEffectFlagIsTrue(String plant, String product, String material);
}
