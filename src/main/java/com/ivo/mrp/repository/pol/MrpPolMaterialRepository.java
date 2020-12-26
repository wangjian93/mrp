package com.ivo.mrp.repository.pol;

import com.ivo.mrp.entity.pol.MrpPolMaterial;
import com.ivo.mrp.key.MrpPolMaterialKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpPolMaterialRepository extends JpaRepository<MrpPolMaterial, MrpPolMaterialKey>, JpaSpecificationExecutor<MrpPolMaterial> {

    List<MrpPolMaterial> findByVer(String ver);
}
