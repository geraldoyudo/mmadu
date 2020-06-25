package com.mmadu.identity.entities;

import com.mmadu.identity.config.MongoDbConfiguration;
import com.mmadu.identity.repositories.TokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.time.ZonedDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Import(MongoDbConfiguration.class)
class TokenTest {
    @Autowired
    private TokenRepository tokenRepository;

    private final ZonedDateTime time = ZonedDateTime.now();

    @Test
    void revokedTokenShouldBeDeleted() throws Exception {
        Token token = getToken();
        token.setRevoked(true);
        token.setRevokedTime(ZonedDateTime.now());
        token = tokenRepository.save(token);
        assertNotNull(token.getId());
        tokenRepository.deleteExpiredAndRevokedTokens(ZonedDateTime.now().plusSeconds(10));
        assertFalse(tokenRepository.existsById(token.getId()));
    }

    private Token getToken() {
        Token token = new Token();
        token.setDomainId("1");
        token.setClientId("2");
        token.setClientIdentifier("3");
        token.setClientInstanceId("4");
        return token;
    }

    @Test
    void revokedTokenWithoutTimeSetShouldNotBeDeleted() throws Exception {
        Token token = getToken();
        token.setRevoked(true);
        token = tokenRepository.save(token);
        assertNotNull(token.getId());
        tokenRepository.deleteExpiredAndRevokedTokens(ZonedDateTime.now().plusSeconds(10));
        assertTrue(tokenRepository.existsById(token.getId()));
    }

    @Test
    void expiredTokenShouldBeDeleted() throws Exception {
        Token token = getToken();
        token.setExpired(true);
        token.setExpiryTime(ZonedDateTime.now());
        token = tokenRepository.save(token);
        assertNotNull(token.getId());
        tokenRepository.deleteExpiredAndRevokedTokens(ZonedDateTime.now().plusSeconds(10));
        assertFalse(tokenRepository.existsById(token.getId()));
    }

    @Test
    void expiredTokenWithoutTimeSetShouldNotBeDeleted() throws Exception {
        Token token = getToken();
        token.setExpired(true);
        token = tokenRepository.save(token);
        assertNotNull(token.getId());
        tokenRepository.deleteExpiredAndRevokedTokens(ZonedDateTime.now().plusSeconds(10));
        assertTrue(tokenRepository.existsById(token.getId()));
    }

    @Test
    void expiredTokenWithoutFlagSetShouldNotBeDeleted() throws Exception {
        Token token = getToken();
        token.setExpiryTime(ZonedDateTime.now());
        token = tokenRepository.save(token);
        assertNotNull(token.getId());
        tokenRepository.deleteExpiredAndRevokedTokens(ZonedDateTime.now().plusSeconds(10));
        assertTrue(tokenRepository.existsById(token.getId()));
    }

    @Test
    void revokedTokenWithoutFlagSetShouldNotBeDeleted() throws Exception {
        Token token = getToken();
        token.setRevokedTime(ZonedDateTime.now());
        token = tokenRepository.save(token);
        assertNotNull(token.getId());
        tokenRepository.deleteExpiredAndRevokedTokens(ZonedDateTime.now().plusSeconds(10));
        assertTrue(tokenRepository.existsById(token.getId()));
    }

    @Test
    void validTokenShouldNotBeDeleted() throws Exception {
        Token token = getToken();
        token = tokenRepository.save(token);
        assertNotNull(token.getId());
        tokenRepository.deleteExpiredAndRevokedTokens(ZonedDateTime.now().plusSeconds(10));
        assertTrue(tokenRepository.existsById(token.getId()));
    }
}