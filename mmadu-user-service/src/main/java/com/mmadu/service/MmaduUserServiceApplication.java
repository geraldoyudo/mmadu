package com.mmadu.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableConfigurationProperties
@EnableSpringDataWebSupport
public class MmaduUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MmaduUserServiceApplication.class, args);
    }
}
