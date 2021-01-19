package com.ivo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author wj
 * @version 1.0
 */
@EnableCaching  //开启缓存
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@EnableSwagger2
public class MrpApplication {

    public static void main(String[] args) {
        SpringApplication.run(MrpApplication.class, args);
    }
}
