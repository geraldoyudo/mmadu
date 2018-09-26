package com.mmadu.service.service;

import static com.mmadu.service.models.AuthenticationStatus.AUTHENTICATED;
import static com.mmadu.service.models.AuthenticationStatus.DOMAIN_INVALID;
import static com.mmadu.service.models.AuthenticationStatus.PASSWORD_INVALID;
import static com.mmadu.service.models.AuthenticationStatus.USERNAME_INVALID;

import com.mmadu.service.entities.AppUser;
import com.mmadu.service.models.AuthenticateRequest;
import com.mmadu.service.models.AuthenticateResponse;
import com.mmadu.service.models.AuthenticationStatus;
import com.mmadu.service.repositories.AppDomainRepository;
import com.mmadu.service.repositories.AppUserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private AppUserRepository appUserRepository;
    private AppDomainRepository appDomainRepository;

    @Autowired
    public void setAppUserRepository(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Autowired
    public void setAppDomainRepository(AppDomainRepository appDomainRepository) {
        this.appDomainRepository = appDomainRepository;
    }

    @Override
    public AuthenticateResponse authenticate(AuthenticateRequest authRequest) {

        if(!appDomainRepository.existsById(authRequest.getDomain())){
            return createAuthenticateResponse(DOMAIN_INVALID);
        }

        Optional<AppUser> userOptional = appUserRepository
                .findByUsernameAndDomainId(authRequest.getUsername(), authRequest.getDomain());

        if (!userOptional.isPresent()) {
            return createAuthenticateResponse(USERNAME_INVALID);
        }

        if (userOptional.get().passwordMatches(authRequest.getPassword())) {
            return createAuthenticateResponse(AUTHENTICATED);
        } else {
            return createAuthenticateResponse(PASSWORD_INVALID);
        }
    }

    private AuthenticateResponse createAuthenticateResponse(AuthenticationStatus status) {
        return AuthenticateResponse.builder().status(status).build();
    }

}
