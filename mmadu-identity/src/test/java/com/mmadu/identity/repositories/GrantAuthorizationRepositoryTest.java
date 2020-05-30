package com.mmadu.identity.repositories;

import com.mmadu.identity.config.MongoDbConfiguration;
import com.mmadu.identity.entities.AuthorizationCodeGrantData;
import com.mmadu.identity.entities.ClientCredentialsGrantData;
import com.mmadu.identity.entities.GrantAuthorization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.time.Duration;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Import(MongoDbConfiguration.class)
class GrantAuthorizationRepositoryTest {
    @Autowired
    private GrantAuthorizationRepository grantAuthorizationRepository;

    @Test
    void testPersistAuthorizationCodeGrant() {
        GrantAuthorization authorization = new GrantAuthorization();
        authorization.setClientInstanceId("2382938");
        AuthorizationCodeGrantData data = new AuthorizationCodeGrantData();
        data.setCode("232983");
        data.setCodeExpiryTime(ZonedDateTime.now());
        authorization.setData(data);
        GrantAuthorization saved = grantAuthorizationRepository.save(authorization);
        GrantAuthorization received = grantAuthorizationRepository.findById(saved.getId()).get();
        AuthorizationCodeGrantData receivedData = (AuthorizationCodeGrantData) received.getData();
        assertAll(
                () -> assertNotNull(saved.getId()),
                () -> assertEquals(data.getCode(), receivedData.getCode()),
                () -> assertEquals(0, Duration.between(data.getCodeExpiryTime(), receivedData.getCodeExpiryTime()).toMinutes())
        );
    }

    @Test
    void testPersistClientCredentialsGrant() {
        GrantAuthorization authorization = new GrantAuthorization();
        authorization.setClientInstanceId("2382938");
        ClientCredentialsGrantData data = new ClientCredentialsGrantData();
        authorization.setData(data);
        authorization = grantAuthorizationRepository.save(authorization);
        assertTrue(grantAuthorizationRepository.findById(authorization.getId()).get().getData() instanceof ClientCredentialsGrantData);
    }
}