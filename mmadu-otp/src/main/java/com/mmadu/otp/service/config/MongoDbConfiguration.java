package com.mmadu.otp.service.config;

import com.mmadu.otp.service.entities.DomainOtpConfiguration;
import com.mmadu.otp.service.entities.OtpProfile;
import com.mmadu.otp.service.entities.OtpToken;
import com.mmadu.otp.service.providers.database.DatabaseCollectionInitializer;
import com.mmadu.otp.service.utils.ZonedDateTimeReadConverter;
import com.mmadu.otp.service.utils.ZonedDateTimeWriteConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
public class MongoDbConfiguration {

    private static final List<Class<?>> COLLECTIONS = List.of(
            DomainOtpConfiguration.class, OtpProfile.class,
            OtpToken.class
    );


    @Bean
    @DependsOn("databaseCollectionInitializer")
    @ConditionalOnProperty(name = "mmadu.transactions.enabled", havingValue = "true")
    public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Bean
    @ConditionalOnProperty(name = "mmadu.transactions.enabled", havingValue = "true")
    public DatabaseCollectionInitializer databaseCollectionInitializer(MongoOperations mongoOperations) {
        DatabaseCollectionInitializer initializer = new DatabaseCollectionInitializer();
        initializer.setCollections(COLLECTIONS);
        initializer.setMongoOperations(mongoOperations);
        return initializer;
    }

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
