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
public class DpsPolKey implements Serializable {

    private String ver;

    private String product;

    private java.sql.Date fabDate;

    public DpsPolKey(String ver, String product, Date fabDate) {
        this.ver = ver;
        this.product = product;
        this.fabDate = fabDate;
    }

    public DpsPolKey() {}
}
