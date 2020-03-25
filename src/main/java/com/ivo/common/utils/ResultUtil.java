package com.ivo.common.utils;

import com.ivo.common.enums.ResultEnum;
import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;

import java.util.Collection;

/**
 * 响应数据(结果)最外层对象工具
 * @author wj
 * @version 1.0
 */
public class ResultUtil {

    /**
     * 操作成功
     * @param msg 提示信息
     * @param object 结果对象
     * @return Result
     */
    public static Result success(String msg, Object object) {
        Result<Object> result = new Result<>();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(msg);
        result.setData(object);
        return result;
    }

    /**
     * 操作成功，使用默认的提示信息
     * @param object 对象
     * @return Result
     */
    public static Result success(Object object) {
        String msg = ResultEnum.SUCCESS.getMessage();
        return success(msg, object);
    }

    /**
     * 操作成功，返回提示信息，不返回数据
     * @param msg 提示信息
     * @return Result
     */
    public static Result success(String msg) {
        return success(msg, null);
    }

    /**
     * 操作成功，使用默认的提示信息，不返回数据
     */
    public static Result success() {
        String msg = ResultEnum.SUCCESS.getMessage();
        return success(msg);
    }

    /**
     * 操作有误
     * @param code 错误码
     * @param msg 提示信息
     * @return Result
     */
    public static Result error(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 操作有误，使用默认400错误码
     * @param msg 提示信息
     * @return Result
     */
    public static Result error(String msg) {
        Integer code = ResultEnum.ERROR.getCode();
        return error(code, msg);
    }

    /**
     * 操作有误，使用默认400错误码，使用默认的提示信息
     * @return Result
     */
    public static Result error() {
        String msg = ResultEnum.ERROR.getMessage();
        return error(msg);
    }

    /**
     * 分页成功返回数据
     * @return PageResult
     */
    public static PageResult successPage(Collection object) {
        PageResult<Collection> pageResult = new PageResult<>();
        // 适应Layui的表格插件，返回状态码改为0
        pageResult.setCode(0);
        pageResult.setMsg(ResultEnum.SUCCESS.getMessage());
        pageResult.setData(object);
        pageResult.setCount(object.size());
        return pageResult;
    }
}
