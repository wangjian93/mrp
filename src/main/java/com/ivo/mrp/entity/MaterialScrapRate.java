//package com.ivo.mrp.entity;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.ivo.common.model.AutoIncreaseEntityModel;
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.persistence.Entity;
//import java.util.Date;
//
///**
// * 物料的损耗率
// * @author wj
// * @version 1.0
// */
//@Setter
//@Getter
//@Entity(name = "mrp_material_scrap_rate")
//public class MaterialScrapRate extends AutoIncreaseEntityModel {
//
//    /**
//     * 物料
//     */
//    private String material;
//
//    /**
//     * 损耗率
//     */
//    private Double scrapRate;
//
//    /**
//     * 生效日期
//     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    private Date effectDate;
//
//    /**
//     * 失效日期
//     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    private Date expireDate;
//
//    /**
//     * 说明
//     */
//    private String memo;
//}
