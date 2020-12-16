package com.ivo.mrp.entity.direct.cell;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CELL机种的MPS命名，数据来源MPS的SD_O_MPSModelDetail
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Cell_Mps_Mode")
public class CellMpsMode {

    /**
     * ID：input_mtrl
     */
    @Id
    private String id;

    /**
     * 机种的PC CellInput命名
     */
    private String cellInput_pc;

    /**
     * CELL料号，即14料号
     */
    private String cellMtrl;
}
