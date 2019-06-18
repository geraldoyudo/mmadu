package com.mmadu.registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class MmaduRegistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MmaduRegistrationApplication.class, args);
    }

}
