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
public class DemandKey implements Serializable {

    /**
     * MRP版本
     */
    private String ver;

    /**
     * 类型
     */
    private String type;

    /**
     * 机种
     */
    private String product;

    private Date fabDate;

    /**
     * 料号
     */
    private String material;

    public DemandKey(String ver, String type, String product, Date fabDate, String material) {
        this.ver = ver;
        this.type = type;
        this.product = product;
        this.fabDate = fabDate;
        this.material = material;
    }

    public DemandKey() {}
}
