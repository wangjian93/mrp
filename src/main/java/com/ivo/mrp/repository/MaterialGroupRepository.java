package com.ivo.mrp.repository;

import com.ivo.mrp.entity.MaterialGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wj
 * @version 1.0
 */
public interface MaterialGroupRepository extends JpaRepository<MaterialGroup, String> {

    /**
     * 分页查询
     * @param searchGroup 模糊查询物料组
     * @param searchName 模糊查询物料组名
     * @return Page<MaterialGroup>
     */
    Page<MaterialGroup> findByMaterialGroupLikeOrMaterialGroupNameLike(String searchGroup, String searchName, Pageable pageable);
}
