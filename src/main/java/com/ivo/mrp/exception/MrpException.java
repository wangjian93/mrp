package com.ivo.mrp.exception;

import com.ivo.exception.BusinessException;

/**
 * MRP业务异常
 * @author wj
 * @version 1.0
 */
public class MrpException extends BusinessException {

    public MrpException() {
        super();
    }

    public MrpException(String message) {
        super(message);
    }

    public MrpException(Integer code, String message) {
        super(code, message);
    }
}
