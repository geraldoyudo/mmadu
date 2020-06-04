package com.mmadu.identity.providers.authorization.code;

public interface DomainAuthorizationCodeGenerator {

    String generateAuthorizationCodeAsDomain(String domainId);
}
