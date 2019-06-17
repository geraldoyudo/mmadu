package com.mmadu.service.config;

import com.geraldoyudo.kweeri.mongo.MongoQueryConverter;
import com.geraldoyudo.kweeri.mongo.MongoQuerySerializer;
import com.mmadu.service.providers.PatchOperationConverter;
import com.mmadu.service.providers.PatchOperationConverterImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

    @Bean
    public MongoQuerySerializer mongoQuerySerializer() {
        return new MongoQuerySerializer();
    }

    @Bean
    public MongoQueryConverter mongoQueryConverter() {
        return new MongoQueryConverter() {
            @Override
            protected String transformProperty(String property) {
                if (property.equals("username") || property.equals("domainId") || property.equals("externalId")) {
                    return property;
                } else {
                    return "properties." + property;
                }
            }
        };
    }

    @Bean
    public PatchOperationConverter patchOperationConverter() {
        return new PatchOperationConverterImpl();
    }
}
