package com.mmadu.tokenservice.populators;

import com.mmadu.tokenservice.services.AppTokenService;
import com.mmadu.tokenservice.utilities.DomainAuthenticationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@DependsOn("tokenPopulator")
public class AdminTokenInitializer {
    private AppTokenService tokenService;

    @Autowired
    public void setTokenService(AppTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostConstruct
    public void initializeAdminToken() {
        tokenService.generateToken(DomainAuthenticationConstants.ADMIN_TOKEN_ID);
    }
}
