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
public class BomAryKey implements Serializable {
    /**
     * 厂别
     */
    private String fab;

    /**
     * 机种 （MPS的命名CellInPut）
     */
    private String product;

    /**
     * ary的成品料号
     */
    private String aryMtrl;

    public BomAryKey(String fab, String product, String aryMtrl) {
        this.fab = fab;
        this.product = product;
        this.aryMtrl = aryMtrl;
    }

    public BomAryKey() {}
}
