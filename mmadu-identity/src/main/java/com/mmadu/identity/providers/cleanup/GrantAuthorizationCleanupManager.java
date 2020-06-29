package com.mmadu.identity.providers.cleanup;

import com.mmadu.identity.repositories.GrantAuthorizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class GrantAuthorizationCleanupManager {
    private GrantAuthorizationRepository grantAuthorizationRepository;

    @Autowired
    public void setGrantAuthorizationRepository(GrantAuthorizationRepository grantAuthorizationRepository) {
        this.grantAuthorizationRepository = grantAuthorizationRepository;
    }

    @Scheduled(cron = "${mmadu.identity.authorization.cleanup-cron:0 0/5 * * * ?}")
    public void cleanUpAuthorizations(){
        grantAuthorizationRepository.deleteExpiredAndRevokedAuthorizations(ZonedDateTime.now());
    }
}
