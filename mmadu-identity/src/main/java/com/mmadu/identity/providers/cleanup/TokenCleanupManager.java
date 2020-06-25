package com.mmadu.identity.providers.cleanup;

import com.mmadu.identity.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class TokenCleanupManager {
    private TokenRepository tokenRepository;

    @Autowired
    public void setTokenRepository(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Scheduled(cron = "${mmadu.identity.token.cleanup-cron:0 0/5 * * * ?}")
    public void cleanUpTokens(){
        tokenRepository.deleteExpiredAndRevokedTokens(ZonedDateTime.now());
    }
}
