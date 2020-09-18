package com.ivo.mrp.repository;

import com.ivo.mrp.entity.Substitute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface SubstituteRepository extends JpaRepository<Substitute, Long>, JpaSpecificationExecutor<Substitute> {

    /**
     * 筛选厂别、机种、物料组、料号
     * @param fab 厂别
     * @param product 机种
     * @param materialGroup 物料组
     * @param material 料号
     * @param validFlag 有效性标识
     * @return Substitute
     */
    Substitute findFirstByFabAndProductAndMaterialGroupAndMaterialAndValidFlag(String fab, String product,
                                                                               String materialGroup, String material,
                                                                               boolean validFlag);

    /**
     * 筛选厂别、机种、物料组
     * @param fab 厂别
     * @param product 机种
     * @param materialGroup 物料组
     * @param validFlag 有效性标识
     * @return  List<Substitute>
     */
    List<Substitute> findByFabAndProductAndMaterialGroupAndValidFlag(String fab, String product, String materialGroup,
                                                         boolean validFlag);

}
