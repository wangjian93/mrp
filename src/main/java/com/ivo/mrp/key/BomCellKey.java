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
public class BomCellKey implements Serializable {

    /**
     * 厂别
     */
    private String fab;

    /**
     * 机种 （MPS的命名CellInPut）
     */
    private String product;

    /**
     * CELL的成品料号
     */
    private String cellMtrl;

    public BomCellKey(String fab, String product, String cellMtrl) {
        this.fab = fab;
        this.product = product;
        this.cellMtrl = cellMtrl;
    }

    public BomCellKey() {}
}
