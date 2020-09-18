package com.ivo.mrp.repository;

import com.ivo.mrp.entity.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wj
 * @version 1.0
 */
public interface MaterialRepository extends JpaRepository<Material, String> {

    /**
     * 分页查询
     * @param searchMaterial 模糊查询料号
     * @param searchMaterialName 模糊查询物料名
     * @return Page<Material>
     */
    Page<Material> findByMaterialLikeOrMaterialNameLike(String searchMaterial, String searchMaterialName, Pageable pageable);
}
