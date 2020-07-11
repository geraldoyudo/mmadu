package com.mmadu.registration.config;

import com.mmadu.registration.entities.DomainFlowConfiguration;
import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import com.mmadu.registration.entities.RegistrationProfile;
import com.mmadu.registration.providers.database.DatabaseCollectionInitializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.List;

@Configuration
public class DatabaseConfig {

    private static final List<Class<?>> COLLECTIONS = List.of(
            DomainFlowConfiguration.class, Field.class, FieldType.class, RegistrationProfile.class
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
}
