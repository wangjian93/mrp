package com.ivo.mrp2.key;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
public class MonthSettlementKey implements Serializable {

    private String month;

    private String plant;

    private String product;

    private String materialGroup;
}
