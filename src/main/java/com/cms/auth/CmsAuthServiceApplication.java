package com.cms.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
//@EnableEurekaClient
public class CmsAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmsAuthServiceApplication.class, args);
    }

}
