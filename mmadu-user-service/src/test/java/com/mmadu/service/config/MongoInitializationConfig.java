package com.mmadu.service.config;

import com.mmadu.service.utilities.MongoManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class MongoInitializationConfig {
    @Bean
    @Lazy(false)
    public MongoManager mongoManager(){
        return new MongoManager();
    }
}
