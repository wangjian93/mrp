package com.ivo.mrp.entity.direct.ary;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Ary_Mps_Mode")
public class AryMpsMode {

    /**
     * ID：input_mtrl
     */
    @Id
    private String id;

    /**
     * 机种的ArrayInPut/SubCellIn命名
     */
    private String product;

    /**
     * CELL料号，即14料号
     */
    private String cellMtrl;
}
