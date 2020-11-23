package com.ivo.mrp.service.impl;

import com.ivo.mrp.entity.Position;
import com.ivo.mrp.repository.PositionRepository;
import com.ivo.mrp.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Service
public class PositionServiceImpl implements PositionService {

    private PositionRepository positionRepository;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Override
    public List<String> getPositionIvoGood() {
        return positionRepository.getPosition(Position.FAB_IVO, Position.TYPE_Good);
    }

    @Override
    public List<String> getPositionIvoDull() {
        return positionRepository.getPosition(Position.FAB_IVO, Position.TYPE_Dull);
    }

    @Override
    public List<String> getPositionIveGood() {
        return positionRepository.getPosition(Position.FAB_IVE, Position.TYPE_Good);
    }

    @Override
    public List<String> getPositionIveDull() {
        return positionRepository.getPosition(Position.FAB_IVE, Position.TYPE_Dull);
    }
}
