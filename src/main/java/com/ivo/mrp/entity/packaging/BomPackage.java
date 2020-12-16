package com.ivo.mrp.entity.packaging;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CELL的包材BOM
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Bom_Package")
public class BomPackage {

    public static final String TYPE_D = "单片";
    public static final String TYPE_L = "连片";

    /**
     * ID: product type linkQty
     */
    @Id
    private String id;

    /**
     * 机种
     */
    private String product;

    /**
     * 单片/连片
     */
    private String type;

    /**
     * 连片数 (连片)
     */
    private String linkQty;

    /**
     * 抽单模式：600抽3/全切单   (单片)
     */
    private String mode;

    /**
     * 切数
     */
    private String cutQty;

    /**
     * 中板数  (单片)
     */
    private String middleQty;

    /**
     * 每个Dense Box或Tray中Panel数目
     */
    private String panelQty;

    private boolean validFlag = true;

    private String memo = "";

    private String creator;

    private Date createDate = new Date();

    /**
     * 构造ID
     * @return String
     */
    public String generateId() {
        if(type.equals(TYPE_D)) {
            return product+"_"+type;
        } else {
            return product+"_"+type+"_"+linkQty;
        }
    }

    @Transient
    List<BomPackageMaterial> bomPackageMaterialList = new ArrayList<>();
}
