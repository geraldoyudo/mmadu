package com.mmadu.service.providers;

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
    private PasswordHasher passwordHasher = new NoOpPasswordHasher();

    @Autowired
    public void setAppUserRepository(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Autowired
    public void setAppDomainRepository(AppDomainRepository appDomainRepository) {
        this.appDomainRepository = appDomainRepository;
    }

    @Autowired
    public void setPasswordHasher(PasswordHasher passwordHasher) {
        this.passwordHasher = passwordHasher;
    }

    @Override
    public AuthenticateResponse authenticate(String domainId, AuthenticateRequest authRequest) {

        if(!appDomainRepository.existsById(domainId)){
            return createAuthenticateResponse(DOMAIN_INVALID);
        }

        Optional<AppUser> userOptional = appUserRepository
                .findByUsernameAndDomainId(authRequest.getUsername(), domainId);

        if (!userOptional.isPresent()) {
            return createAuthenticateResponse(USERNAME_INVALID);
        }

        if (passwordHasher.matches(authRequest.getPassword(), userOptional.get().getPassword())) {
            return createAuthenticateResponse(AUTHENTICATED);
        } else {
            return createAuthenticateResponse(PASSWORD_INVALID);
        }
    }

    private AuthenticateResponse createAuthenticateResponse(AuthenticationStatus status) {
        return AuthenticateResponse.builder().status(status).build();
    }

}
