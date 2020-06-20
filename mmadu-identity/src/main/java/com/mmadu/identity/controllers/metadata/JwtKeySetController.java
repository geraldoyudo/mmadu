package com.mmadu.identity.controllers.metadata;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.exceptions.CredentialNotFoundException;
import com.mmadu.identity.exceptions.DomainNotFoundException;
import com.mmadu.identity.providers.credentials.CredentialsLoader;
import com.mmadu.identity.services.domain.DomainIdentityConfigurationService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/metadata/{domainId}")
@Slf4j
public class JwtKeySetController {
    private DomainIdentityConfigurationService domainIdentityConfigurationService;
    private CredentialsLoader<RSAKey> credentialsLoader;

    @Autowired
    public void setDomainIdentityConfigurationService(DomainIdentityConfigurationService domainIdentityConfigurationService) {
        this.domainIdentityConfigurationService = domainIdentityConfigurationService;
    }

    @Autowired
    public void setCredentialsLoader(CredentialsLoader<RSAKey> credentialsLoader) {
        this.credentialsLoader = credentialsLoader;
    }

    @GetMapping("/jwks.json")
    public JSONObject getKeys(@PathVariable("domainId") String domainId) {
        DomainIdentityConfiguration configuration = domainIdentityConfigurationService.findByDomainId(domainId)
                .orElseThrow(DomainNotFoundException::new);
        Map<String, Object> properties = Optional.ofNullable(configuration.getAccessTokenProperties())
                .orElse(Collections.emptyMap());
        String credentialId = (String) Optional.ofNullable(properties.get("credentialId"))
                .orElseThrow(CredentialNotFoundException::new);
        try {
            RSAKey key = credentialsLoader.loadCredentialById(credentialId);
            return new JWKSet(key.toPublicJWK()).toJSONObject();
        } catch (Exception ex) {
            log.error("could not fetch credentials", ex);
            throw new CredentialNotFoundException();
        }
    }
}
