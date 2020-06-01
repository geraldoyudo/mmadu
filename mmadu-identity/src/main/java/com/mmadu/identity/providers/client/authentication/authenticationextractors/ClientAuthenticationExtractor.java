package com.mmadu.identity.providers.client.authentication.authenticationextractors;

import com.mmadu.identity.providers.client.authentication.MmaduClientAuthenticationToken;

import javax.servlet.ServletRequest;
import java.util.Optional;

public interface ClientAuthenticationExtractor {

    Optional<MmaduClientAuthenticationToken> extractAuthentication(ServletRequest request);
}
