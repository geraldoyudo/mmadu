package com.mmadu.identity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MmaduIdentityApplication {

    public static void main(String[] args) {
        SpringApplication.run(MmaduIdentityApplication.class, args);
    }

}
