package com.ivo.mrp.service.impl;

import com.ivo.mrp.entity.Project;
import com.ivo.mrp.repository.ProjectRepository;
import com.ivo.mrp.service.ProjectService;
import com.ivo.rest.RestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;

    private RestService restService;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository,
                              RestService restService) {
        this.projectRepository = projectRepository;
        this.restService = restService;
    }

    @Override
    public void syncProject() {
        //从81数据库的表BG_O_Project同步数据
        log.info("机种数据同步>> START");
        List<Map> mapList = restService.getProject();
        if(mapList == null || mapList.size()==0) return;
        for(Map map : mapList) {
            Project project = new Project();
            project.setProject((String) map.get("project"));
            project.setApplication((String) map.get("application"));
            project.setSize((String) map.get("size"));
            project.setCut(Double.valueOf( (String)map.get("cut") ));
            projectRepository.save(project);
        }
        log.info("机种数据同步>> END");
    }
}
