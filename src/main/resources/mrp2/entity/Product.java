package com.ivo.mrp2.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 机种
 * @author wj
 * @version 1.0
 */
@Entity
@Table(name = "mrp2_product")
@Setter
@Getter
public class Product {

    @Id
    private String product;

    /**
     * 创建时间
     */
    private Date createDate = new Date();

    /**
     * 备注
     */
    private String memo;
}
