package com.ivo.mrp.service;

import com.ivo.mrp.entity.direct.ary.DpsAryOutputName;
import com.ivo.mrp.entity.direct.cell.DpsCellOutputName;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface DpsOutputNameService {

    void saveDpsCellOutputName(List<DpsCellOutputName> list);

    List<DpsCellOutputName> getDpsCellOutputName(String ver, String outputName);

    void saveDpsAryOutputName(List<DpsAryOutputName> list);

    List<DpsAryOutputName> getDpsAryOutputName(String ver, String outputName);
}
