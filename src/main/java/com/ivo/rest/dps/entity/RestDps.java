package com.ivo.rest.dps.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

/**
 * DPS
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
public class RestDps {
    /**
     * 厂
     */
    private String fab_id;

    /**
     * 日期
     */
    private Date fab_date;

    /**
     * model
     */
    private String model_id;

    /**
     * 产品
     */
    private String prod_id;

    /**
     * 数量
     */
    private Double bpc_qty;

    /**
     * dps版本
     */
    private String dps_ver;
}
