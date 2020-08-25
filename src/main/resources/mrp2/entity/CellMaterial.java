package com.ivo.mrp2.entity;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CELL机种的成品料号，来源DPS的SD_O_MPSModelDetail
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "mrp2_cell_material")
public class CellMaterial extends AutoIncreaseEntityModel {

    /**
     * 机种
     */
    private String project;

    /**
     * CELL INPUT
     */
    private String cellInput;

    /**
     * 成品料号
     */
    private String material;
}
