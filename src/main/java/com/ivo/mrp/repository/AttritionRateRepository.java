//package com.ivo.mrp.repository;
//
//import com.ivo.mrp.entity.AttritionRate;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import java.util.Date;
//import java.util.List;
//
///**
// * @author wj
// * @version 1.0
// */
//public interface AttritionRateRepository extends JpaRepository<AttritionRate, Long> {
//
//    /**
//     * 获取date日期时的有效损耗率
//     * @param venderCode 供应商Code
//     * @param material 料号
//     * @param date 日期
//     * @return List<AttritionRate>
//     */
//    @Query("from AttritionRate a where a.venderCode=?1 and a.material=?2 and a.effectDate<=?3 and a.expireDate>?3")
//    List<AttritionRate> getEffectAttritionRate(String venderCode, String material, Date date);
//
//    /**
//     * 获取date日期时的还未等到生效数据
//     * @param venderCode 供应商Code
//     * @param material 料号
//     * @param date 日期
//     * @return List
//     */
//    @Query("from AttritionRate a where a.venderCode=?1 and a.material=?2 and a.effectDate>?3 and a.expireDate>?3")
//    List<AttritionRate> getNoEffectAttritionRate(String venderCode, String material, Date date);
//
//    /**
//     * 条件模糊查询，查询date日期已生效数据
//     * @param venderCode 供应商Code
//     * @param vender 供应商
//     * @param materialGroup 物料组
//     * @param material 料号
//     * @param date 日期
//     * @return List
//     */
//    @Query("from AttritionRate a where a.venderCode like CONCAT(?1, '%') and a.vender like CONCAT(?2, '%') and a.materialGroup like CONCAT(?3, '%') and a.material like CONCAT(?4, '%') and a.effectDate<=?5 and a.expireDate>?5")
//    List<AttritionRate> getEffectAttritionRate(String venderCode, String vender, String materialGroup, String material, Date date);
//
//    /**
//     * 条件模糊查询，查询date日期已生效数据
//     * @param venderCode 供应商Code
//     * @param vender 供应商
//     * @param materialGroup 物料组
//     * @param material 料号
//     * @return List
//     */
//    @Query("from AttritionRate a where a.venderCode like CONCAT(?1, '%') and a.vender like CONCAT(?2, '%') and a.materialGroup like CONCAT(?3, '%') and a.material like CONCAT(?4, '%')")
//    List<AttritionRate> getAttritionRate(String venderCode, String vender, String materialGroup, String material);
//}
