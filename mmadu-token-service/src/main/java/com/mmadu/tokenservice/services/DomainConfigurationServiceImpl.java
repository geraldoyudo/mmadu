package com.mmadu.tokenservice.services;

import com.mmadu.tokenservice.entities.DomainConfiguration;
import com.mmadu.tokenservice.exceptions.DomainConfigurationNotFoundException;
import com.mmadu.tokenservice.exceptions.TokenNotFoundException;
import com.mmadu.tokenservice.repositories.AppTokenRepository;
import com.mmadu.tokenservice.repositories.DomainConfigurationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

import static com.mmadu.tokenservice.utilities.DomainAuthenticationConstants.ADMIN_TOKEN_ID;

@Service
public class DomainConfigurationServiceImpl implements DomainConfigurationService {
    private static final Logger logger = LoggerFactory.getLogger(DomainConfigurationServiceImpl.class);

    public static final String GLOBAL_DOMAIN_CONFIG = "0";
    private DomainConfigurationRepository domainConfigurationRepository;
    private AppTokenService appTokenService;
    private AppTokenRepository appTokenRepository;

    @Autowired
    public void setDomainConfigurationRepository(DomainConfigurationRepository domainConfigurationRepository) {
        this.domainConfigurationRepository = domainConfigurationRepository;
    }

    @Autowired
    public void setAppTokenService(AppTokenService appTokenService) {
        this.appTokenService = appTokenService;
    }

    @Autowired
    public void setAppTokenRepository(AppTokenRepository appTokenRepository) {
        this.appTokenRepository = appTokenRepository;
    }

    @Override
    public DomainConfiguration getConfigurationForDomain(String domainId) {

        if (domainConfigurationRepository.existsByDomainId(domainId)) {
            return domainConfigurationRepository.findByDomainId(domainId).get();
        } else {
            return domainConfigurationRepository.findByDomainId(GLOBAL_DOMAIN_CONFIG)
                    .orElseThrow(() -> new DomainConfigurationNotFoundException());
        }
    }

    @PostConstruct
    public void init() {
        if (!domainConfigurationRepository.existsById(GLOBAL_DOMAIN_CONFIG)) {
            DomainConfiguration configuration = new DomainConfiguration();
            configuration.setDomainId("");
            configuration.setId(GLOBAL_DOMAIN_CONFIG);
            configuration.setAuthenticationApiToken("");
            domainConfigurationRepository.save(configuration);
        }
    }

    @Override
    public boolean tokenMatchesDomain(String token, String domainId) {
        if (appTokenService.tokenMatches(ADMIN_TOKEN_ID, token)) {
            return true;
        }
        if (domainId.equals("admin")) {
            return false;
        }
        try {
            DomainConfiguration configuration = getConfigurationForDomain(domainId);
            String tokenId = configuration.getAuthenticationApiToken();
            if (StringUtils.isEmpty(tokenId)) {
                configuration = getConfigurationForDomain(GLOBAL_DOMAIN_CONFIG);
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
    public void setAuthTokenForDomain(String tokenId, String domainId) {
        if (!appTokenRepository.existsById(tokenId)) {
            throw new TokenNotFoundException();
        }
        DomainConfiguration configuration = new DomainConfiguration();
        configuration.setDomainId(domainId);
        configuration.setAuthenticationApiToken(tokenId);
        domainConfigurationRepository.save(configuration);
    }
}
