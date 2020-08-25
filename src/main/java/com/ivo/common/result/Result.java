package com.ivo.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 响应数据(结果)最外层对象
 * @author wj
 * @version 1.0
 */
@ApiModel("响应数据")
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 7531446044859643177L;

    /**
     * 状态码
     */
    @ApiModelProperty("状态码")
    private Integer code;

    /**
     * 提示信息
     */
    @ApiModelProperty("提示信息")
    private String msg;

    /**
     * 响应数据
     */
    @ApiModelProperty("响应数据")
    private T data;
}
