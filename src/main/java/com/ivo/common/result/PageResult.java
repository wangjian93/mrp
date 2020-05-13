package com.ivo.common.result;

import lombok.Getter;
import lombok.Setter;

/**
 * 响应数据分页增强
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
public class PageResult<T> extends Result<T> {

    /**
     * 响应数据的总量，用于分页
     */
    private long count;

}
