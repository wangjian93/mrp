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
public class MrpKey implements Serializable {
    /**
     * 版本
     */
    private String ver;

    /**
     * 日期
     */
    private java.sql.Date fabDate;

    /**
     * 料号
     */
    private String material;

    public MrpKey(String ver, Date fabDate, String material) {
        this.ver = ver;
        this.fabDate = fabDate;
        this.material = material;
    }

    public MrpKey() {}
}
