package com.ivo.mrp.repository;

import com.ivo.mrp.entity.MpsVer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wj
 * @version 1.0
 */
public interface MpsVerRepository extends JpaRepository<MpsVer, String> {

    /**
     * 分页查询
     * @param searchFab 模糊查询厂别
     * @param searchVer 模糊查询版本
     * @param pageable 分页
     * @return  Page<DpsVer>
     */
    Page<MpsVer> findByFabLikeAndVerLikeAndValidFlagIsTrue(String searchFab, String searchVer, Pageable pageable);
}
