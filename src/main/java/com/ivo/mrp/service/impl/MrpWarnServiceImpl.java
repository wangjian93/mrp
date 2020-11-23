package com.ivo.mrp.service.impl;

import com.ivo.mrp.entity.MrpWarn;
import com.ivo.mrp.repository.MrpWarnRepository;
import com.ivo.mrp.service.MrpWarnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Service
public class MrpWarnServiceImpl implements MrpWarnService {

    private MrpWarnRepository mrpWarnRepository;

    @Autowired
    public MrpWarnServiceImpl(MrpWarnRepository mrpWarnRepository) {
        this.mrpWarnRepository = mrpWarnRepository;
    }

    @Override
    public void addWarn(String ver, String product, String type, String memo) {
        MrpWarn mrpWarn = new MrpWarn();
        mrpWarn.setVer(ver);
        mrpWarn.setProduct(product);
        mrpWarn.setType(type);
        mrpWarn.setMemo(memo);
        mrpWarnRepository.save(mrpWarn);
    }

    @Override
    public List<MrpWarn> getMrpWarn(String ver) {
        return mrpWarnRepository.findByVer(ver);
    }
}
