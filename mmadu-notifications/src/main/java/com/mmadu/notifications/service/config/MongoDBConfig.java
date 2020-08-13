package com.mmadu.notifications.service.config;

import com.mmadu.notifications.service.entities.*;
import com.mmadu.notifications.service.provider.database.DatabaseCollectionInitializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.List;

@Configuration
public class MongoDBConfig {

    private static final List<Class<?>> COLLECTIONS = List.of(
            DomainNotificationConfiguration.class, NotificationProfile.class,
            ProviderConfiguration.class, ScheduledEventNotificationMessage.class,
            ScheduledUserNotificationMessage.class
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
