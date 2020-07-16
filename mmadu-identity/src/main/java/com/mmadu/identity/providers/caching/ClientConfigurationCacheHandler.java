package com.mmadu.identity.providers.caching;

import com.mmadu.identity.entities.Client;
import com.mmadu.identity.entities.ClientInstance;
import com.mmadu.identity.repositories.ClientInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class ClientConfigurationCacheHandler {
    private ClientConfigurationCacheManager clientConfigurationCacheManager;
    private ClientInstanceRepository clientInstanceRepository;

    @Autowired
    public void setClientInstanceRepository(ClientInstanceRepository clientInstanceRepository) {
        this.clientInstanceRepository = clientInstanceRepository;
    }

    @Autowired
    public void setClientConfigurationCacheManager(ClientConfigurationCacheManager clientConfigurationCacheManager) {
        this.clientConfigurationCacheManager = clientConfigurationCacheManager;
    }

    @HandleAfterDelete
    @HandleAfterSave
    public void evictOnClientInstanceChange(ClientInstance instance) {
        clientConfigurationCacheManager.evictClientInstanceFromCache(instance.getIdentifier());
    }

    @HandleAfterSave
    @HandleAfterDelete
    public void evictOnClientChange(Client client) {
        clientInstanceRepository.findByDomainIdAndClientId(client.getDomainId(), client.getId(), Pageable.unpaged())
                .forEach(instance -> clientConfigurationCacheManager.evictClientInstanceFromCache(instance.getIdentifier()));
    }
}
