package com.mmadu.service.config;

import com.geraldoyudo.kweeri.mongo.MongoQueryConverter;
import com.geraldoyudo.kweeri.mongo.MongoQuerySerializer;
import com.mmadu.service.entities.*;
import com.mmadu.service.providers.PatchOperationConverter;
import com.mmadu.service.providers.PatchOperationConverterImpl;
import com.mmadu.service.providers.database.DatabaseCollectionInitializer;
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
            AppDomain.class, AppUser.class, Authority.class,
            Group.class, Role.class, RoleAuthority.class,
            UserAuthority.class, UserGroup.class, UserRole.class
    );

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
