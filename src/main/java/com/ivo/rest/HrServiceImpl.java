package com.ivo.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Service
@Slf4j
public class HrServiceImpl implements HrService {

    private final static String ORG_URL = "http://myivo.ivo.com.cn/org/";

    @Override
    public boolean verify(String user, String password) {
        RestTemplate restTemplate = new RestTemplate();
        String url = ORG_URL+"org/verify";
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("eid", user);
        map.add("password", password);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, map, String.class);
        String r = responseEntity.getBody();
        return Boolean.valueOf(r);
    }

    @Override
    public Map getEmployee(String user) {
        RestTemplate restTemplate = new RestTemplate();
        String url = ORG_URL+"org/getEmployee";
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("eid", user);
        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(url, map, HashMap.class);
        Map r = responseEntity.getBody();
        return r;
    }
}