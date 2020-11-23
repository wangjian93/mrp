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
public class BomAryMtrlKey implements Serializable {

    /**
     * 厂别
     */
    private String fab;

    /**
     * 机种
     */
    private String product;

    /**
     * ary的成品料号
     */
    private String aryMtrl;

    /**
     * 料号
     */
    private String material;

    public BomAryMtrlKey(String fab, String product, String aryMtrl, String material) {
        this.fab = fab;
        this.product = product;
        this.aryMtrl = aryMtrl;
        this.material = material;
    }

    public BomAryMtrlKey() {}
}
