package com.mmadu.security.providers.converters;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

public interface JwtAuthenticationConversionStrategy {

    boolean apply(Jwt jwt);

    AbstractAuthenticationToken convert(Jwt jwt);
}
