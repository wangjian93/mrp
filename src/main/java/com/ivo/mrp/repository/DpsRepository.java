//package com.ivo.mrp.repository;
//
//import com.ivo.mrp.entity.Dps;
//import com.ivo.mrp.entity.PrimaryKey;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.Date;
//import java.util.List;
//
///**
// * @author wj
// * @version 1.0
// */
//public interface DpsRepository extends JpaRepository<Dps, PrimaryKey> {
//
//    /**
//     * 根据DPS的版本查询
//     * @param ver DPS的版本
//     * @return List<RestDps>
//     */
//    List<Dps> findByVer(String ver);
//
//    /**
//     * @param ver 版本
//     * @param fab 厂
//     * @param product 产品
//     * @param fabDate 日期
//     * @return DPS
//     */
//    Dps getDpsByVerAndFabAndProductAndFabDate(String ver, String fab, String product, Date fabDate);
//
//}
