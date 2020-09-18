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
public class BomCellMtrlKey implements Serializable {

    /**
     * 厂别
     */
    private String fab;

    /**
     * 机种
     */
    private String product;

    /**
     * cell的成品料号
     */
    private String cellMtrl;

    /**
     * 料号
     */
    private String material;

    public BomCellMtrlKey(String fab, String product, String cellMtrl, String material) {
        this.fab = fab;
        this.product = product;
        this.cellMtrl = cellMtrl;
        this.material = material;
    }

    public BomCellMtrlKey() {}
}
