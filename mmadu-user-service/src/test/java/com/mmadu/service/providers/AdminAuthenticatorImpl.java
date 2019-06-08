package com.mmadu.service.providers;

import static com.mmadu.service.utilities.DomainAuthenticationConstants.ADMIN_TOKEN_ID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminAuthenticatorImpl implements AdminAuthenticator {

    private AppTokenService tokenService;

    @Autowired
    public void setTokenService(AppTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean isTokenAdmin(String token) {
        return tokenService.tokenMatches(ADMIN_TOKEN_ID, token);
    }
}
