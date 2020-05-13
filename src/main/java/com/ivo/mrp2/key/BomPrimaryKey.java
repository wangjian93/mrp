package com.ivo.mrp2.key;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * BOM主键
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
public class BomPrimaryKey implements Serializable {

    private String plant;

    private String product;

    private String material;
}
