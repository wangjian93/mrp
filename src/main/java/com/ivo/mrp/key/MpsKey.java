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
public class MpsKey implements Serializable {

    /**
     * 版本
     */
    private String ver;

    /**
     * 机种
     */
    private String product;

    /**
     * 日期
     */
    private java.sql.Date fabDate;

    public MpsKey(String ver, String product, Date fabDate) {
        this.ver = ver;
        this.product = product;
        this.fabDate = fabDate;
    }

    public MpsKey(){}
}
