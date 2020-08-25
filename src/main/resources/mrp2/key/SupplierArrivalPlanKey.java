package com.ivo.mrp2.key;

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
public class SupplierArrivalPlanKey implements Serializable {

    /**
     * 厂别
     */
    private String plant;

    /**
     * 日期
     */
    private Date date;

    /**
     * 料号
     */
    private String material;

    /**
     * 厂商
     */
    private String supplier;

    public SupplierArrivalPlanKey(String plant, Date date, String material, String supplier) {
        this.plant = plant;
        this.date = date;
        this.material = material;
        this.supplier = supplier;
    }

    public SupplierArrivalPlanKey() {}
}
