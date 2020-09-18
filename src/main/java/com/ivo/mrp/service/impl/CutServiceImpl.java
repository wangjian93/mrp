package com.ivo.mrp.service.impl;

import com.ivo.mrp.entity.Project;
import com.ivo.mrp.service.CutService;
import com.ivo.mrp.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class CutServiceImpl implements CutService {

    private ProjectService projectService;

    @Autowired
    public CutServiceImpl(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public Double getProjectCut(String project) {
        Project o = projectService.getProject(project);
        return o == null ? null : o.getCut();
    }
}
