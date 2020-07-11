package com.mmadu.identity.config;

import com.mmadu.identity.entities.*;
import com.mmadu.identity.entities.credentials.Credential;
import com.mmadu.identity.entities.keys.Key;
import com.mmadu.identity.providers.database.DatabaseCollectionInitializer;
import com.mmadu.identity.utils.converters.ZonedDateTimeReadConverter;
import com.mmadu.identity.utils.converters.ZonedDateTimeWriteConverter;
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
            Credential.class, Key.class, Client.class, ClientInstance.class,
            DomainIdentityConfiguration.class, GrantAuthorization.class,
            Resource.class, Scope.class, Token.class
    );

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(
                List.of(
                        new ZonedDateTimeReadConverter(),
                        new ZonedDateTimeWriteConverter()
                )
        );
    }

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
