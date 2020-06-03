package com.mmadu.identity.providers.token.providers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.identity.entities.token.JwtTokenCredentials;
import com.mmadu.identity.entities.token.TokenCredentials;
import com.mmadu.identity.exceptions.TokenCreationException;
import com.mmadu.identity.models.token.ClaimConfiguration;
import com.mmadu.identity.models.token.ClaimSpecs;
import com.mmadu.identity.models.token.TokenSpecification;
import com.mmadu.identity.providers.credentials.CredentialsLoader;
import com.mmadu.identity.providers.token.claims.ClaimGenerator;
import com.mmadu.identity.services.security.CredentialService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtTokenProvider implements TokenProvider {
    private static final String CREDENTIAL_ID_PROPERTY = "credentialId";

    public static final String TYPE = "jwt";

    private CredentialService credentialService;
    private ObjectMapper objectMapper;
    private ClaimGenerator claimGenerator;
    private CredentialsLoader<RSAKey> credentialsLoader;

    @Autowired
    @Qualifier("jwt")
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setCredentialService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @Autowired
    public void setClaimGenerator(ClaimGenerator claimGenerator) {
        this.claimGenerator = claimGenerator;
    }

    @Autowired
    public void setCredentialsLoader(CredentialsLoader<RSAKey> credentialsLoader) {
        this.credentialsLoader = credentialsLoader;
    }

    @Override
    public String providerId() {
        return TYPE;
    }

    @Override
    public TokenCredentials create(TokenSpecification specification) {
        Map<String, Object> properties = Optional.ofNullable(specification.getConfiguration())
                .orElse(Collections.emptyMap());
        String credentialId = (String) Optional.ofNullable(properties.get(CREDENTIAL_ID_PROPERTY))
                .orElseThrow(() -> new IllegalStateException("credential not configured"));
        try {
            RSAKey rsaKey = credentialsLoader.loadCredentialById(credentialId);
            JWSSigner signer = new RSASSASigner(rsaKey);
            ClaimConfiguration claimConfiguration = (ClaimConfiguration) Optional.ofNullable(properties.get("claim"))
                    .orElse(new ClaimConfiguration());
            JWTClaimsSet claimsSet = convertToClaimSet(claimGenerator.generateClaim(
                    specification.getGrantAuthorization(), ClaimSpecs.builder()
                            .type(specification.getType())
                            .configuration(claimConfiguration)
                            .build()
            ));

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256)
                            .keyID(rsaKey.getKeyID()).build(),
                    claimsSet);

            signedJWT.sign(signer);
            JwtTokenCredentials credentials = new JwtTokenCredentials();
            credentials.setToken(signedJWT.serialize());
            credentials.setJti(signedJWT.getHeader().getKeyID());
            return credentials;
        } catch (JOSEException ex) {
            throw new TokenCreationException("could not create token", ex);
        }
    }

    private JWTClaimsSet convertToClaimSet(Object object) {
        TypeReference<HashMap<String, Object>> typeRef
                = new TypeReference<>() {
        };
        Map<String, Object> properties = objectMapper.convertValue(object, typeRef);
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
        properties.forEach(builder::claim);
        return builder.build();
    }
}
