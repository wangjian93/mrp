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
public class MrpPolMaterialKey implements Serializable {

    private String ver;

    private String product;

    private String material;

    public MrpPolMaterialKey(String ver, String product, String material) {
        this.ver = ver;
        this.product = product;
        this.material = material;
    }

    public MrpPolMaterialKey() {}
}
