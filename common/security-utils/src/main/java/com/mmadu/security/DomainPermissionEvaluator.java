package com.mmadu.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.Optional;

public class DomainPermissionEvaluator implements PermissionEvaluator {
    private Logger logger = LoggerFactory.getLogger(DomainPermissionEvaluator.class);

    private DomainTokenChecker domainTokenChecker;


    public void setDomainTokenChecker(DomainTokenChecker domainTokenChecker) {
        this.domainTokenChecker = domainTokenChecker;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object o, Object permissionObject) {
        String token = Optional.ofNullable((String) authentication.getPrincipal()).orElse("");
        String domainId = (String) permissionObject;
        return domainTokenChecker.checkIfTokenMatchesDomainToken(token, domainId);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return true;
    }
}
