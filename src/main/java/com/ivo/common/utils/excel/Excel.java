package com.ivo.common.utils.excel;

import java.lang.annotation.*;

/**
 * ClassName: Excel
 * Function:  Excel字段注解
 * Date:      2019/7/12 23:36
 * author     likaixuan
 * version    V1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Excel {

    /**
     * Excel中表头字段
     */
    String title();
}
