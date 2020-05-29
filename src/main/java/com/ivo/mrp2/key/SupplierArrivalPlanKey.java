package com.ivo.mrp2.key;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
public class SupplierArrivalPlanKey implements Serializable {

    /**
     * 供应商编号
     */
    private String supplierCode;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    /**
     * 料号
     */
    private String material;

    /**
     * MRP版本
     */
    private String mrpVer;

    private String plant;
}
