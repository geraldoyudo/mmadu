package com.mmadu.identity.providers.database;

import org.springframework.data.mongodb.core.MongoOperations;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

public class DatabaseCollectionInitializer {
    private List<Class<?>> collections = Collections.emptyList();
    private MongoOperations mongoOperations;

    public void setCollections(List<Class<?>> collections) {
        this.collections = collections;
    }

    public void setMongoOperations(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @PostConstruct
    public void setUp() {
        this.collections.stream()
                .filter(c -> !mongoOperations.collectionExists(c))
                .forEach(mongoOperations::createCollection);
    }
}
