package com.mmadu.identity.services.resource;

import com.mmadu.identity.entities.Resource;

import java.util.List;
import java.util.Optional;

public interface ResourceService {

    Optional<Resource> findByDomainIdAndIdentifier(String domainId, String identifier);

    boolean existsByDomainIdAndIdentifier(String domainId, String identifier);

    boolean areAllResourcesSupportedInDomain(String domainId, List<String> resources);

    boolean supportsTokenCategory(String domainId, List<String> resources, String tokenCategory);
}
