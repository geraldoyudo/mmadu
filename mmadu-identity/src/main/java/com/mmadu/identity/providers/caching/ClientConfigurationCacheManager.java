package com.mmadu.identity.providers.caching;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
@CacheConfig(cacheNames = "clients")
@Slf4j
public class ClientConfigurationCacheManager {

    @CacheEvict(key = "#clientIdentifier")
    public void evictClientInstanceFromCache(String clientIdentifier) {
        log.debug("client instance {} evicted from cache", clientIdentifier);
    }
}
