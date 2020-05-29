package com.ivo.mrp2.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 机种的切片数
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity(name = "mrp_product_slice")
public class ProductSlice {

    /**
     * 机种
     */
    @Id
    private String product;

    /**
     * 切片数
     */
    private double slice;
}
