package com.mmadu.service.security;

import static com.mmadu.service.utilities.DomainAuthenticationConstants.ADMIN_TOKEN_ID;

import com.mmadu.service.entities.DomainConfiguration;
import com.mmadu.service.models.DomainIdObject;
import com.mmadu.service.repositories.AppUserRepository;
import com.mmadu.service.service.AppTokenService;
import com.mmadu.service.service.DomainConfigurationService;
import java.io.Serializable;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
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

    @Override
    public boolean hasPermission(Authentication authentication, Object o, Object permissionObject) {
        String domain = (String) o;
        String permission;
        if(domain.equals("user")){
            String userId = (String) permissionObject;
            Optional<DomainIdObject> domainIdObject = appUserRepository.findDomainIdForUser(userId);
            if(!domainIdObject.isPresent()){
                return false;
            }else {
                permission = domainIdObject.get().getDomainId();
            }
        }else {
            permission = (String) permissionObject;
        }
        String token = (String) authentication.getPrincipal();
        if (token == null) {
            token = "";
        }
        if(permission.equals("admin")){
            return appTokenService.tokenMatches(ADMIN_TOKEN_ID, token);
        }else{
            if(appTokenService.tokenMatches(ADMIN_TOKEN_ID, token))
                return true;

            try {
                String domainId = permission;
                DomainConfiguration configuration = domainConfigurationService.getConfigurationForDomain(domainId);
                String tokenId = configuration.getAuthenticationApiToken();
                if (StringUtils.isEmpty(tokenId)) {
                    configuration = domainConfigurationService.getConfigurationForDomain(DomainConfigurationService.GLOBAL_DOMAIN_CONFIG);
                    tokenId = configuration.getAuthenticationApiToken();
                }
                return appTokenService.tokenMatches(tokenId, token);
            }catch (Exception ex){
                logger.warn("An error occurred while evaluating permission: {}: {}. Rejecting permission.",
                        ex.getClass().getName(), ex.getMessage());
                return  false;
            }
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return true;
    }
}
