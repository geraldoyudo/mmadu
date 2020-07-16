package com.mmadu.identity.providers.caching;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
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
@CacheConfig(cacheNames = {
        "domainConfigurations",
        "verificationKeys"
})
public class DomainConfigurationCacheManager {

    @HandleAfterSave
    @HandleAfterDelete
    @CacheEvict(key = "#configuration.domainId")
    public void updateDomainConfigurationCache(DomainIdentityConfiguration configuration) {
        log.debug("Evicting domain configuration cache for domain {}", configuration.getDomainId());
    }
}
