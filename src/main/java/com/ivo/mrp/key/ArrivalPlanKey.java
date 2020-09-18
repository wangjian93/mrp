package com.ivo.mrp.key;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
public class ArrivalPlanKey implements Serializable {

    /**
     * 厂别
     */
    private String fab;

    /**
     * 料号
     */
    private String material;

    /**
     * 日期
     */
    private java.sql.Date fabDate;

    /**
     * 供应商ID
     */
    private String supplierCode;
}
