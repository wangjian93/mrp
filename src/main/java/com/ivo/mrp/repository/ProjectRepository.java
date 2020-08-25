package com.ivo.mrp.repository;

import com.ivo.mrp.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wj
 * @version 1.0
 */
public interface ProjectRepository extends JpaRepository<Project, String> {
}
