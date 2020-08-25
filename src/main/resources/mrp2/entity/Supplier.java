package com.ivo.mrp2.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author wj
 * @version 1.0
 */
@Entity
@Table(name = "mrp2_supplier")
@Setter
@Getter
public class Supplier {

    /**
     * 厂商ID
     */
    @Id
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 简称
     */
    private String sName;

    public Supplier(String id, String name, String sName) {
        this.id = id;
        this.name = name;
        this.sName = sName;
    }

    public Supplier() {}
}
