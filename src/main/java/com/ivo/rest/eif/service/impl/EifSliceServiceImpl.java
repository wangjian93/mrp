package com.ivo.rest.eif.service.impl;

import com.ivo.mrp2.entity.ProductSlice;
import com.ivo.rest.eif.mapper.SliceMapper;
import com.ivo.rest.eif.service.EifSliceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Service
@Slf4j
public class EifSliceServiceImpl implements EifSliceService {

    private SliceMapper sliceMapper;

    @Autowired
    public EifSliceServiceImpl(SliceMapper sliceMapper) {
        this.sliceMapper = sliceMapper;
    }

    @Override
    public List<ProductSlice> getProductSlice() {
        return sliceMapper.getProductSlice();
    }
}
