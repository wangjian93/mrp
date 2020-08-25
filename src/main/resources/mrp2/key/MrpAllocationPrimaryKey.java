package com.ivo.mrp2.key;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author wj
 * @version 1.0
 */
public class MrpAllocationPrimaryKey implements Serializable {

    private String plant;

    private Date fabDate;

    private String material;

    private String supplier;

    public MrpAllocationPrimaryKey(String plant, Date fabDate, String material, String supplier) {
        this.plant = plant;
        this.fabDate = fabDate;
        this.material = material;
        this.supplier = supplier;
    }

    public MrpAllocationPrimaryKey() {}
}
