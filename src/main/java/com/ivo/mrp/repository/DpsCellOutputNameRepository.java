package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.cell.DpsCellOutputName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface DpsCellOutputNameRepository extends JpaRepository<DpsCellOutputName, Long> {

    List<DpsCellOutputName> findByVerAndOutputName(String ver, String outputName);

}
