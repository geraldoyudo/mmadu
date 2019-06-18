package com.mmadu.registration.config;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VelocityConfig {

    @Bean
    public VelocityEngine velocityEngine(){
        VelocityEngine engine = new VelocityEngine();
        engine.init();
        return engine;
    }
}
