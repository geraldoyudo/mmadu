package com.mmadu.service.security.domainidextractors;

import com.mmadu.service.repositories.AppUserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppUserIdDomainIdExtractor implements DomainIdExtractor {
    private AppUserRepository appUserRepository;

    @Autowired
    public void setAppUserRepository(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public Optional<String> extractDomainId(Object permissionObject) {
        return appUserRepository.findDomainIdForUser((String) permissionObject)
                .map(domainIdObject -> domainIdObject.getDomainId());
    }

    @Override
    public String domain() {
        return "user";
    }
}
