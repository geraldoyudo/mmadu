package com.mmadu.identity.providers.client.authentication;

import com.mmadu.identity.models.client.UnauthenticatedClient;
import com.mmadu.identity.providers.client.authentication.authenticationextractors.ClientAuthenticationExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class ClientAuthenticationFilter implements Filter {
    private List<ClientAuthenticationExtractor> authenticationExtractors = Collections.emptyList();

    @Autowired(required = false)
    public void setAuthenticationExtractors(List<ClientAuthenticationExtractor> authenticationExtractors) {
        this.authenticationExtractors = authenticationExtractors;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        boolean authenticated = false;
        for (ClientAuthenticationExtractor extractor : authenticationExtractors) {
            Optional<MmaduClientAuthenticationToken> clientAuthentication = extractor.extractAuthentication(servletRequest);
            if (clientAuthentication.isPresent()) {
                SecurityContextHolder.getContext().setAuthentication(clientAuthentication.get());
                authenticated = true;
                break;
            }
        }
        if (!authenticated) {
            SecurityContextHolder.getContext().setAuthentication(
                    new MmaduClientAuthenticationToken(new UnauthenticatedClient())
            );
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
