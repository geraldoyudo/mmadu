package com.mmadu.registration.utils;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class VelocityEngineConfig {

    @Bean
    public VelocityEngine velocityEngine(){
        VelocityEngine engine = new VelocityEngine();
        engine.init();
        return engine;
    }
}
