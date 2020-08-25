package com.ivo.mrp2.entity;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "mrp2_product_customer")
public class ProductCustomer extends AutoIncreaseEntityModel {

    /**
     * 机种
     */
    private String product;

    /**
     * 客户
     */
    private String customer;

}
