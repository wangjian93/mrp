package com.ivo.exception;

/**
 * 自定义异常基类
 * @author wj
 * @version 1.0
 */
public abstract class IException extends RuntimeException {

    private Integer code;

    IException() {
        super();
    }

    IException(String message) {
        super(message);
    }

    IException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
