package com.mmadu.service.utilities;

import com.mmadu.service.populators.DomainPopulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class MongoManager {
    private static final Logger logger = LoggerFactory.getLogger(MongoManager.class);

    @Autowired
    private DomainPopulator domainPopulator;

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void setUp() {
        logger.info("Wiping Databaase");
        mongoTemplate.getDb().drop();
        domainPopulator.setUpDomains();
        logger.info("Repopulated database");
    }

    @PreDestroy
    public void shutDown() {
        logger.info("Shutting down and Wiping database");
        mongoTemplate.getDb().drop();
    }
}
