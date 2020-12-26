package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.ary.DpsAryOutputName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface DpsAryOutputNameRepository extends JpaRepository<DpsAryOutputName, Long> {

    List<DpsAryOutputName> findByVerAndOutputName(String ver, String outputName);

}
