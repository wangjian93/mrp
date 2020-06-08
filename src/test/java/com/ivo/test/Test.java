package com.ivo.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public class Test {


    public static void main(String[] args) throws IOException {
        String v = "{\"v\": 1040423.002}";
        ObjectMapper objectMapper = new ObjectMapper();
        Map m = objectMapper.readValue(v, Map.class);

        JSONObject jsonObject = JSONObject.fromObject(v);

        String str=new BigDecimal(jsonObject.getDouble("v")).toString();
        System.out.println(m.get("v"));
        System.out.println(jsonObject.getDouble("v"));
        System.out.println(str);
        System.out.println(jsonObject);
    }
}
