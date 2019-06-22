package com.mmadu.security;

public interface DomainTokenChecker {

    boolean checkIfTokenMatchesDomainToken(String token, String domainId);

}
