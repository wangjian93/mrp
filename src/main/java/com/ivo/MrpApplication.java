package com.ivo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author wj
 * @version 1.0
 */
@SpringBootApplication
@EnableTransactionManagement
public class MrpApplication {

    public static void main(String[] args) {
        SpringApplication.run(MrpApplication.class, args);
    }
}
