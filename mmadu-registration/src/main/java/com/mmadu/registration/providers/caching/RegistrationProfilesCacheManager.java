package com.mmadu.registration.providers.caching;

import com.mmadu.registration.entities.RegistrationProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RepositoryEventHandler
@CacheConfig(cacheNames = "registrationProfiles")
public class RegistrationProfilesCacheManager {

    @HandleAfterDelete
    @HandleAfterSave
    @CacheEvict(key = "#profile.domainId + '-' + #profile.code")
    public void evictRegistrationProfileFromCache(RegistrationProfile profile) {
        log.debug("Removing registration profile from cache: {} {}", profile.getDomainId(), profile.getCode());
    }
}
