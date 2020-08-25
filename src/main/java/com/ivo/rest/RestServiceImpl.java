package com.ivo.rest;

import com.ivo.rest.eif.EifMapper;
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
public class RestServiceImpl implements RestService {

    private EifMapper eifMapper;

    @Autowired
    public RestServiceImpl(EifMapper eifMapper) {
        this.eifMapper = eifMapper;
    }

    @Override
    public List<Map> getMaterialGroup() {
        log.info("从81数据库的表MM_O_MaterialGroup同步数据");
        return eifMapper.getMaterialGroup();
    }

    @Override
    public List<Map> getMaterial() {
        log.info("从81数据库的表MM_O_Material同步数据");
        return eifMapper.getMaterial();
    }

    @Override
    public List<Map> getProject() {
        log.info("从81数据库的表BG_O_Project同步数据");
        return eifMapper.getProject();
    }
}
