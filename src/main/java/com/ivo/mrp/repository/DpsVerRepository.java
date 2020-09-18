package com.ivo.mrp.repository;

import com.ivo.mrp.entity.DpsVer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface DpsVerRepository extends JpaRepository<DpsVer, String> {

    /**
     * 筛选PC的dps文件版本
     * @param ver dps文件版本
     * @param type 类型
     * @return List<DpsVer>
     */
    List<DpsVer> findByDpsFileAndType(String ver, String type);

    /**
     * 分页查询
     * @param searchFab 模糊查询厂别
     * @param searchType 模糊查询类型
     * @param searchVer 模糊查询版本
     * @param pageable 分页
     * @return  Page<DpsVer>
     */
    Page<DpsVer> findByFabLikeAndTypeLikeAndVerLikeAndValidFlagIsTrue(String searchFab, String searchType, String searchVer, Pageable pageable);
}
