package com.ivo.mrp.service.impl;

import com.ivo.mrp.entity.direct.cell.DpsCellOutputName;
import com.ivo.mrp.repository.DpsCellOutputNameRepository;
import com.ivo.mrp.service.DpsOutputNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Service
public class DpsOutputNameServiceImpl implements DpsOutputNameService {

    private DpsCellOutputNameRepository dpsCellOutputNameRepository;

    @Autowired
    public DpsOutputNameServiceImpl(DpsCellOutputNameRepository dpsCellOutputNameRepository) {
        this.dpsCellOutputNameRepository = dpsCellOutputNameRepository;
    }

    @Override
    public void saveDpsCellOutputName(List<DpsCellOutputName> list) {
        dpsCellOutputNameRepository.saveAll(list);
    }

    @Override
    public List<DpsCellOutputName> getDpsCellOutputName(String ver, String outputName) {
        return dpsCellOutputNameRepository.findByVerAndOutputName(ver, outputName);
    }
}
