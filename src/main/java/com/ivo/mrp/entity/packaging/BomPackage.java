package com.ivo.mrp.entity.packaging;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
public class BomPackage extends AutoIncreaseEntityModel {

    public static final String TYPE_D = "单片";
    public static final String TYPE_L = "连片";

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
    private Double linkQty;

    /**
     * 抽单模式：600抽3/全切单   (单片)
     */
    private String mode;

    /**
     * 切数
     */
    private Double cutQty;

    /**
     * 中板数  (单片)
     */
    private Double middleQty;

    /**
     * 每个Dense Box或Tray中Panel数目
     */
    private Double panelQty;

    @OneToMany(mappedBy="bomPackage", cascade= CascadeType.ALL)
    private List<BomPackageMaterial> materialList = new ArrayList<>();

    private boolean validFlag = true;

    private String memo = "";

    private String creator;

    private Date createDate = new Date();
}
