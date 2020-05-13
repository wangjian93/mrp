package com.ivo.common.utils;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HttpServlet工具类，获取HttpServlet子对象
 * @author wj
 * @version 1.0
 */
public class HttpServletUtil {

    /**
     * 获取ServletRequestAttributes对象
     */
    public static ServletRequestAttributes getServletRequest() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * 获取HttpServletRequest对象
     */
    public static HttpServletRequest getRequest() {
        return getServletRequest().getRequest();
    }

    /**
     * 获取HttpServletResponse对象
     */
    public static HttpServletResponse getResponse() {
        return getServletRequest().getResponse();
    }

    /**
     * 获取请求参数
     */
    public static String getParameter(String param) {
        return getRequest().getParameter(param);
    }

    /**
     * 获取请求参数，带默认值
     */
    public static String getParameter(String param, String defaultValue) {
        String value = getParameter(param);
        return StringUtils.isEmpty(value) ? defaultValue : value;
    }

    /**
     * 获取请求参数转换为int类型
     */
    public static Integer getParameterInt(String param){
        return Integer.valueOf(getRequest().getParameter(param));
    }

    /**
     * 获取请求参数转换为int类型，带默认值
     */
    public static Integer getParameterInt(String param, Integer defaultValue){
        return Integer.valueOf(getParameter(param, String.valueOf(defaultValue)));
    }
}
