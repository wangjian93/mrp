package com.ivo.common.enums;

import lombok.Getter;

/**
 * 后台返回结果集枚举
 * @author wj
 * @version 1.0
 */
@Getter
public enum ResultEnum {

    /**
     * 通用状态
     */
    SUCCESS(200, "成功"),
    ERROR(500, "错误"),

    /**
     * 账户问题
     */
    USER_EXIST(401, "用户已经存在"),
    USER_NOT_FOUND(402, "用户不存在"),
    USER_NAME_PWD_NULL(405, "用户名和密码不能为空"),
    USER_CAPTCHA_ERROR(406, "验证码错误"),

    /**
     * 角色问题
     */
    ROLE_EXIST(401, "角色已存在"),
    ROLE_NOT_FOUNT(402, "角色不存在"),

    /**
     * 菜单资源问题
     */
    RESOURCE_NOT_FOUND(402, "请求资源不存在"),


    /**
     * Project
     */
    PROJECT_EXIST(500, "机种已存在"),
    PROJECT_NOT_FOUND(500, "机种不存在"),
    PROJECT_NAME_EMPTY(500, "机种名不能为空"),

    ;

    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
