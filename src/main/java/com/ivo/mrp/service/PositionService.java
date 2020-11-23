package com.ivo.mrp.service;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface PositionService {

    /**
     * 获取IVO良品仓位
     * @return List<String>
     */
    List<String> getPositionIvoGood();

    /**
     * 获取IVO呆滞仓位
     * @return List<String>
     */
    List<String> getPositionIvoDull();

    /**
     * 获取IVE良品仓位
     * @return List<String>
     */
    List<String> getPositionIveGood();

    /**
     * 获取IVE呆滞仓位
     * @return List<String>
     */
    List<String> getPositionIveDull();

}
