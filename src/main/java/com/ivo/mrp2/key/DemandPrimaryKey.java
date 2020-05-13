package com.ivo.mrp2.key;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;

/**
 * Demand主键
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
public class DemandPrimaryKey implements Serializable {

    private String dpsVer;

    private Date fabDate;

    private String product;

    private String material;
}
