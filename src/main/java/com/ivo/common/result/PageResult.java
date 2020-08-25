package com.ivo.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 响应数据分页增强
 * @author wj
 * @version 1.0
 */
@ApiModel("响应数据分页")
@Setter
@Getter
public class PageResult<T> extends Result<T> {

    /**
     * 响应数据的总量，用于分页
     */
    @ApiModelProperty("响应数据的总量")
    private long count;

}
