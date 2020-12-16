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
public class DpsPackageKey implements Serializable {

    private String ver;

    private String packageId;

    private java.sql.Date fabDate;

    public DpsPackageKey(String ver, String packageId, Date fabDate) {
        this.ver = ver;
        this.packageId = packageId;
        this.fabDate = fabDate;
    }

    public DpsPackageKey() {}
}
