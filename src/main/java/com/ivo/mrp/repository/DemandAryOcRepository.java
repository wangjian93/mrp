package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.ary.DemandAryOc;
import com.ivo.mrp.key.DemandKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wj
 * @version 1.0
 */
public interface DemandAryOcRepository extends JpaRepository<DemandAryOc, DemandKey> {
}
