package com.mmadu.security.providers.authorization;

import javax.servlet.http.HttpServletRequest;

public interface DomainLimiter {

    void limitRequestToDomain(HttpServletRequest request);
}
