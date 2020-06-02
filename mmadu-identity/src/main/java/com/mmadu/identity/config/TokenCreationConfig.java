package com.mmadu.identity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmadu.identity.utils.Jackson8Module;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;

@Configuration
public class TokenCreationConfig {

    @Bean
    @Qualifier("jwt")
    public ObjectMapper jwtTokenGenerationObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Jackson8Module module = new Jackson8Module();
        module.addStringSerializer(ZonedDateTime.class, (val) -> Long.toString(val.toInstant().toEpochMilli() / 1000));
        objectMapper.registerModule(module);
        return objectMapper;
    }

}
