package com.mmadu.service.integration;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {"mmadu.domain.encrypt-keys=true"})
@ActiveProfiles("IT")
public class AuthenticationApiDomainTokenSecurityWithTokenEncryptionIT extends AuthenticationApiDomainTokenSecurityIT {
    private static final String TOKEN = "22222222222222222222222222222222";

    @Override
    protected String getToken() {
        return TOKEN;
    }
}
