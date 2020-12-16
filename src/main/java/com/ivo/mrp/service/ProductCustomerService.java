package com.ivo.mrp.service;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface ProductCustomerService {

    List<String> getCustomer(String product);


    List<String> getCustomer(List<String> product);
}
