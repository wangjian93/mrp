package com.ivo.mrp.key;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 料号联合主键
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
public class MaterialKey implements Serializable {

    /**
     * 厂别 LCM1/LCM2/ARY/CELL
     */
    private String fab;

    /**
     * 机种
     */
    private String product;

    /**
     * 料号
     */
    private String material;

    public MaterialKey(String fab, String product, String material) {
        this.fab = fab;
        this.product = product;
        this.material = material;
    }

    public MaterialKey() {}
}
