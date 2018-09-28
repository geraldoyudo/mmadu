package com.mmadu.service.integration;

import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {"mmadu.domain.authenticate-api-security-enabled=true",
"mmadu.domain.encrypt-keys=true"})
public class AuthenticationApiDomainTokenSecurityWithTokenEncryptionIT extends AuthenticationApiDomainTokenSecurityIT {

}
