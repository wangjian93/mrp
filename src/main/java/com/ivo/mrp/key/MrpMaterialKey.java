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
public class MrpMaterialKey implements Serializable {

    /**
     * MRP版本
     */
    private String ver;

    /**
     * 料号
     */
    private String material;

    public MrpMaterialKey(String ver, String material) {
        this.ver = ver;
        this.material = material;
    }

    public MrpMaterialKey() {}
}
