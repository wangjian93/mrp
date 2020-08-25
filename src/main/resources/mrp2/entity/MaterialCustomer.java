package com.ivo.mrp2.entity;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 材料与客户
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "mrp2_Material_Customer")
public class MaterialCustomer extends AutoIncreaseEntityModel {

    /**
     * 料号
     */
    private String material;

    /**
     * 供应商ID
     */
    private String customer;
}
