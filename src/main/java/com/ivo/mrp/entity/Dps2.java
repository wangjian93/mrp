//package com.ivo.mrp.entity;
//
//import com.ivo.common.model.AutoIncreaseEntityModel;
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Entity;
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//import java.util.List;
//
///**
// * 生产计划
// * @author wj
// * @version 1.0
// */
//@Getter
//@Setter
//@Entity
//@Table(name = "mrp_dps2")
//public class Dps2 extends AutoIncreaseEntityModel {
//
//    /**
//     * 机种
//     */
//    private String model;
//
//    /**
//     * 产品
//     */
//    private String product;
//
//    /**
//     * 版本
//     */
//    private String version;
//
//    private String month;
//
//    @OneToMany( mappedBy="dps2", cascade = CascadeType.ALL)
//    private List<DpsDetail2> dpsDetail2s;
//}
