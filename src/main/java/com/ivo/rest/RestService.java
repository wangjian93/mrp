package com.ivo.rest;

import java.util.List;
import java.util.Map;

/**
 * 接口访问服务
 * @author wj
 * @version 1.0
 */
public interface RestService {

    /**
     * 从81数据库的表MM_O_MaterialGroup同步数据
     * @return List<Map>
     */
    List<Map> getMaterialGroup();

    /**
     * 从81数据库的表MM_O_Material同步数据
     * @return List<Map>
     */
    List<Map> getMaterial();

    /**
     * 从81数据库的表BG_O_Project同步数据
     * @return List<Map>
     */
    List<Map> getProject();
}
