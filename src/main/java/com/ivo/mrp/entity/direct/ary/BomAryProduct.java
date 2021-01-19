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
@Table(name = "MRP3_Bom_Ary_Product")
public class BomAryProduct {

    /**
     * 机种
     */
    @Id
    private String product;

    /**
     * 验证通过
     */
    private boolean  isVerify = false;
}
