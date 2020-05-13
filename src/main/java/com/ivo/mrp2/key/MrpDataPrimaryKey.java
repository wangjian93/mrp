package com.ivo.mrp2.key;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;

/**
 * MRP数据的主键
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
public class MrpDataPrimaryKey implements Serializable {

    private String mrpVer;

    private Date fabDate;

    private String material;

    public MrpDataPrimaryKey(String mrpVer, Date fabDate, String material) {
        this.mrpVer = mrpVer;
        this.fabDate = fabDate;
        this.material = material;
    }

    public MrpDataPrimaryKey() {
    }
}
