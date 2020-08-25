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
public class MrpDataMasterPrimaryKey implements Serializable {
    private String mrpVer;

    private String material;
}
