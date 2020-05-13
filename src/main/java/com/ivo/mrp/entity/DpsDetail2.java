//package com.ivo.mrp.entity;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.ivo.common.model.AutoIncreaseEntityModel;
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.persistence.Entity;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//import java.util.Date;
//
///**
// * @author wj
// * @version 1.0
// */
//@Setter
//@Getter
//@Entity
//@Table(name = "mrp_dps_detail")
//public class DpsDetail2 extends AutoIncreaseEntityModel {
//
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    private Date date;
//
//    private Double quantity;
//
//    @ManyToOne
//    @JoinColumn(name = "dps_fk")
//    @JsonIgnore
//    private Dps2 dps2;
//}
