package com.mmadu.service.security;

import static com.mmadu.service.utilities.DomainAuthenticationConstants.ADMIN_TOKEN_ID;

import com.mmadu.service.entities.DomainConfiguration;
import com.mmadu.service.repositories.AppUserRepository;
import com.mmadu.service.security.domainidextractors.DomainIdExtractor;
import com.mmadu.service.service.AppTokenService;
import com.mmadu.service.service.DomainConfigurationService;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DomainPermissionEvaluator implements PermissionEvaluator {
    private Logger logger = LoggerFactory.getLogger(DomainPermissionEvaluator.class);

    private AppTokenService appTokenService;
    private DomainConfigurationService domainConfigurationService;
    private AppUserRepository appUserRepository;
    private Map<String, DomainIdExtractor> domainIdExtractorMap = new HashMap<>();

    @Autowired
    public void setAppTokenService(AppTokenService appTokenService) {
        this.appTokenService = appTokenService;
    }

    @Autowired
    public void setDomainConfigurationService(DomainConfigurationService domainConfigurationService) {
        this.domainConfigurationService = domainConfigurationService;
    }

    @Autowired
    public void setAppUserRepository(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Autowired
    public void setDomainIdExtractors(List<DomainIdExtractor> domainIdExtractors){
        domainIdExtractorMap = new HashMap<>();
        domainIdExtractors
                .forEach(domainIdExtractor -> domainIdExtractorMap.put(domainIdExtractor.domain(), domainIdExtractor));
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object o, Object permissionObject) {
        String domain = (String) o;
        DomainIdExtractor domainIdExtractor = domainIdExtractorMap.get(domain);
        if(domainIdExtractor == null){
            return false;
        }
        Optional<String> domainIdValue = domainIdExtractor.extractDomainId(permissionObject);
        if(!domainIdValue.isPresent()){
            return false;
        }
        String token = Optional.ofNullable((String) authentication.getPrincipal()).orElse("");
        if(appTokenService.tokenMatches(ADMIN_TOKEN_ID, token)) {
            return true;
        }
        String domainId = domainIdValue.get();
        if(domainId.equals("admin")){
            return false;
        }
        return checkIfTokenMatchesDomainToken(token, domainId);
    }

    private boolean checkIfTokenMatchesDomainToken(String token, String domainId) {
        try {
            DomainConfiguration configuration = domainConfigurationService.getConfigurationForDomain(domainId);
            String tokenId = configuration.getAuthenticationApiToken();
            if (StringUtils.isEmpty(tokenId)) {
                configuration = domainConfigurationService.getConfigurationForDomain(DomainConfigurationService.GLOBAL_DOMAIN_CONFIG);
                tokenId = configuration.getAuthenticationApiToken();
            }
            return appTokenService.tokenMatches(tokenId, token);
        } catch (Exception ex) {
            logger.warn("An error occurred while evaluating permission: {}: {}. Rejecting permission.",
                    ex.getClass().getName(), ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return true;
    }
}
