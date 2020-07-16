package com.mmadu.identity.providers.caching;

import com.mmadu.identity.entities.credentials.Credential;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@CacheConfig(cacheNames = "rsaKeys")
@RepositoryEventHandler
public class CredentialCacheHandler {

    @HandleBeforeSave
    @HandleBeforeDelete
    @CacheEvict(key = "#credential.id", condition = "#credential.type eq 'rsa'")
    public void evictRsaKeyCache(Credential credential){
        log.debug("Evicting rsa key cache for credential {}", credential.getId());
    }
}
