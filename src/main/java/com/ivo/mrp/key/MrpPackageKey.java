package com.ivo.mrp.key;

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
public class MrpPackageKey implements Serializable {

    /**
     * MRP版本
     */
    private String ver;

    private String packageId;

    /**
     * 料号
     */
    private String material;

    private Date fabDate;
}
