package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MaterialRepository extends JpaRepository<Material, String> {

    /**
     * 查询所有料号
     * @return List<String>
     */
    @Query(value = "select material from mrp2_material m where m.material like :search limit :limit", nativeQuery = true)
    List<String> searchMaterial(@Param("search") String search, @Param("limit") int limit);

    /**
     * 查询物料组下的料号
     * @param materialGroup 物料组
     * @return  List<String>
     */
    @Query(value = "select material from Material m where m.materialGroup=:materialGroup")
    List<String> findMaterialByMaterialGroup(@Param("materialGroup") String materialGroup);
}
