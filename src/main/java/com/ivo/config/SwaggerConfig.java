package com.ivo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置
 * @author wj
 * @version 1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 用于配置swagger2，包含文档基本信息
     * 指定swagger2的作用域（这里指定包路径下的所有API）
     * @return Docket
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //指定需要扫描的controller
                .apis(RequestHandlerSelectors.basePackage("com.ivo.mrp.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 构建文档基本信息，用于页面显示，可以包含版本、
     * 联系人信息、服务地址、文档描述信息等
     * @return ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //标题
                .title("MRP系统API")
                .termsOfServiceUrl("http://localhost:8082")
                //设置联系方式
                .version("1.0")
                .build();
    }
}


