package com.ivo.mrp.key;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
public class PackageAllocationKey implements Serializable {

    private String packageId;

    private String material;

    private String supplierCode;

    private java.sql.Date fabDate;
}
