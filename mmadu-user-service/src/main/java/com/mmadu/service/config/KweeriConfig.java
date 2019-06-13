package com.mmadu.service.config;

import com.geraldoyudo.kweeri.mongo.MongoQueryConverter;
import com.geraldoyudo.kweeri.mongo.MongoQuerySerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KweeriConfig {

    @Bean
    public MongoQuerySerializer mongoQuerySerializer() {
        return new MongoQuerySerializer();
    }

    @Bean
    public MongoQueryConverter mongoQueryConverter() {
        return new MongoQueryConverter() {
            @Override
            protected String transformProperty(String property) {
                if (property.equals("username") || property.equals("domainId")) {
                    return property;
                } else {
                    return "properties." + property;
                }
            }
        };
    }
}
