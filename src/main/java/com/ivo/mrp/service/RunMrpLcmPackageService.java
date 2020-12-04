package com.ivo.mrp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wj
 * @version 1.0
 */
@Service
public class RunMrpLcmPackageService {

    @Autowired
    private RunMrpService runMrpService;

    public void run() {
        runMrpService.computeDemand("20201204044");
    }
}
