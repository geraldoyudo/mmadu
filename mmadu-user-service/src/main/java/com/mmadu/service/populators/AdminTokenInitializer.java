package com.mmadu.service.populators;

import com.mmadu.service.providers.AppTokenService;
import com.mmadu.service.utilities.DomainAuthenticationConstants;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn("tokenPopulator")
public class AdminTokenInitializer {
    private AppTokenService tokenService;

    @Autowired
    public void setTokenService(AppTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostConstruct
    public void initializeAdminToken(){
        tokenService.generateToken(DomainAuthenticationConstants.ADMIN_TOKEN_ID);
    }
}
