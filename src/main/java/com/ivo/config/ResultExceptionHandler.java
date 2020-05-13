package com.ivo.config;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;

/**
 * 全局统一异常处理
 * @author wj
 * @version 1.0
 */
@ControllerAdvice
public class ResultExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ResultExceptionHandler.class);

    /**
     * 拦截ServletException
     * @param e 异常
     * @return
     */
    @ExceptionHandler(ServletException.class)
    @ResponseBody
    public Result servletException(ServletException e) {
        logger.error("【请求异常】", e);
        return ResultUtil.error(500, "请求失败--" + e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     * @param e 异常
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Result runtimeException(RuntimeException e) {
        logger.error("【系统异常】", e);
        return ResultUtil.error(500, "未知错误--" + e.getMessage());
    }
}
