package com.ivo.mrp.entity;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

/**
 * 每月月初库存表
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "mrp3_Inventory")
public class Inventory extends AutoIncreaseEntityModel {

    private Date fabDate;

    private String MATNR;

    private String MATKL;

    private String WERKS;

    private String LGORT;

    private String MEINS;

    private Double LABST;
}
