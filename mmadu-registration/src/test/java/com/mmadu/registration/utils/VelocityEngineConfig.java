package com.mmadu.registration.utils;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

@TestConfiguration
public class VelocityEngineConfig {

    @Bean
    public VelocityEngine velocityEngine(){
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        VelocityEngine engine = new VelocityEngine();
        engine.init(p);
        return engine;
    }
}
