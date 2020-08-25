package com.ivo.mrp2.key;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;

/**
 * DPS主键
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
public class DpsPrimaryKey implements Serializable {

    private String dpsVer;

    private Date fabDate;

    private String product;

    public DpsPrimaryKey(String dpsVer, String product, Date fabDate) {
        this.dpsVer = dpsVer;
        this.fabDate = fabDate;
        this.product = product;
    }

    public DpsPrimaryKey() {}
}
