package com.ivo.mrp.repository;

import com.ivo.mrp.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author wj
 * @version 1.0
 */
public interface ProjectRepository extends JpaRepository<Project, String> {

    /**
     * 分页查询
     * @param searchProject 模糊查询机种
     * @param pageable 分页
     * @return Page<Project>
     */
    Page<Project> findByProjectLike(@Param("searchProject") String searchProject, Pageable pageable);
}
