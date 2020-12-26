package com.ivo.mrp.repository.pol;

import com.ivo.mrp.entity.pol.MrpPol;
import com.ivo.mrp.key.MrpPolKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpPolRepository extends JpaRepository<MrpPol, MrpPolKey> {

    List<MrpPol> findByVerAndProductAndMaterial(String ver, String product, String material);
}
