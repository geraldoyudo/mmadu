package com.mmadu.security;

@Deprecated
public interface DomainTokenChecker {

    boolean checkIfTokenMatchesDomainToken(String token, String domainId);

}
