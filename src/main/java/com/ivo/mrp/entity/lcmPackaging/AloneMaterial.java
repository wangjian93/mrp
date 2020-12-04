package com.ivo.mrp.entity.lcmPackaging;

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
@Table(name = "MRP3_Bom_Packaging_Lcm_alone")
public class AloneMaterial extends AutoIncreaseEntityModel {

    private String fab;

    private String materialGroup;

    private String materialGroupName;

    private String material;

    private String materialName;
}
