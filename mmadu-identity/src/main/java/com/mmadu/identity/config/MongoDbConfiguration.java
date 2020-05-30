package com.mmadu.identity.config;

import com.mmadu.identity.utils.converters.ZonedDateTimeReadConverter;
import com.mmadu.identity.utils.converters.ZonedDateTimeWriteConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
public class MongoDbConfiguration {

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(
                List.of(
                        new ZonedDateTimeReadConverter(),
                        new ZonedDateTimeWriteConverter()
                )
        );
    }
}
