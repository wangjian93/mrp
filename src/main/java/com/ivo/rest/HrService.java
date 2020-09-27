package com.ivo.rest;

import java.util.Map;

/**
 * HR服务
 * 访问ORG的接口
 * @author wj
 * @version 1.0
 */
public interface HrService {

    /**
     * 验证密码
     * @param user 用户工号
     * @param password 密码
     * @return boolean
     */
    boolean verify(String user, String password);

    /**
     * 获取员工信息
     * @param user 用户工号
     * @return Map
     */
    Map getEmployee(String user);
}
