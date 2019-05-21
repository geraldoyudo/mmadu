package com.mmadu.service.utilities;

import com.mmadu.service.populators.AdminTokenInitializer;
import com.mmadu.service.populators.DomainPopulator;
import com.mmadu.service.populators.TokenPopulator;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoManager {
    private static final Logger logger = LoggerFactory.getLogger(MongoManager.class);

    @Autowired
    private AdminTokenInitializer adminTokenInitializer;
    @Autowired
    private DomainPopulator domainPopulator;
    @Autowired
    private TokenPopulator tokenPopulator;

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void setUp(){
        logger.info("Wisping Databaase");
        mongoTemplate.getDb().drop();
        domainPopulator.setUpDomains();
        tokenPopulator.setUpTokens();
        adminTokenInitializer.initializeAdminToken();
        logger.info("Repopulated database");
    }

    @PreDestroy
    public void shutDown(){
        logger.info("Shutting down and Wiping database");
        mongoTemplate.getDb().drop();
    }
}
