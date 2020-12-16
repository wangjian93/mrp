package com.ivo.mrp.repository;

import com.ivo.mrp.entity.MrpVer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpVerRepository extends JpaRepository<MrpVer, String> {

    /**
     * 分页查询
     * @param searchFab 模糊查询厂别
     * @param searchType 模糊查询类型
     * @param searchVer 模糊查询版本
     * @param pageable 分页
     * @return  Page<DpsVer>
     */
    Page<MrpVer> findByFabLikeAndTypeLikeAndVerLikeAndValidFlagIsTrue(String searchFab, String searchType, String searchVer, Pageable pageable);

    MrpVer findTopByFabAndTypeAndValidFlagIsTrueOrderByVerDesc(String fab, String type);

}
