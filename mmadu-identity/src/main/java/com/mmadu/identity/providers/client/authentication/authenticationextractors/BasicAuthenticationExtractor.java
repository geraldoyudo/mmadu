package com.mmadu.identity.providers.client.authentication.authenticationextractors;

import com.mmadu.identity.entities.ClientSecretCredentials;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.providers.client.authentication.MmaduClientAuthenticationToken;
import com.mmadu.identity.services.client.MmaduClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BasicAuthenticationExtractor implements ClientAuthenticationExtractor {
    private static final Pattern AUTHORIZATION_PATTERN = Pattern.compile("Basic (.*)");

    private MmaduClientService mmaduClientService;

    @Autowired
    public void setMmaduClientService(MmaduClientService mmaduClientService) {
        this.mmaduClientService = mmaduClientService;
    }

    @Override
    public Optional<MmaduClientAuthenticationToken> extractAuthentication(ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            String authString = ((HttpServletRequest) request).getHeader(HttpHeaders.AUTHORIZATION);
            if (authString == null) {
                authString = "";
            }
            Matcher matcher = AUTHORIZATION_PATTERN.matcher(authString);
            if (matcher.matches()) {
                String authorizationBase64 = matcher.group(1);
                String authorization = new String(Base64Utils.decodeFromString(authorizationBase64), StandardCharsets.UTF_8);
                String[] components = authorization.split(":");
                if (components.length == 2) {
                    String clientIdentifier = components[0];
                    String clientSecret = components[1];
                    Optional<MmaduClient> client = mmaduClientService.loadClientByIdentifier(clientIdentifier);
                    if (client.isPresent() && client.get().getCredentials() != null &&
                            (client.get().getCredentials() instanceof ClientSecretCredentials)
                            && client.get().getCredentials().matches(clientSecret)) {
                        return Optional.of(new MmaduClientAuthenticationToken(client.get()));
                    }
                }
            }
        }
        return Optional.empty();
    }
}
