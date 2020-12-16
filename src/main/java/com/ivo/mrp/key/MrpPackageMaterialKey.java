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
public class MrpPackageMaterialKey implements Serializable {

    /**
     * MRP版本
     */
    private String ver;

    private String packageId;

    /**
     * 料号
     */
    private String material;

    public MrpPackageMaterialKey(String ver, String packageId, String material) {
        this.ver = ver;
        this.packageId = packageId;
        this.material = material;
    }

    public MrpPackageMaterialKey() {}
}
