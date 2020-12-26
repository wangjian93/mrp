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
public class MrpPolKey implements Serializable {

    private String ver;

    private String product;

    private String material;

    private Date fabDate;
}
