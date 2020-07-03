package com.mmadu.identity.providers.token.providers;

import com.mmadu.identity.entities.token.AlphanumericTokenCredentials;
import com.mmadu.identity.models.token.TokenCreationResult;
import com.mmadu.identity.models.token.TokenSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
public class AlphaNumericTokenProvider implements TokenProvider {
    public static final String TYPE = "alphanumeric";
    @Value("${mmadu.tokens.alphanumeric.default-token-length:20}")
    private int defaultTokenLength = 20;

    @Override
    public String providerId() {
        return TYPE;
    }

    @Override
    public TokenCreationResult create(TokenSpecification specification) {
        AlphanumericTokenCredentials credentials = new AlphanumericTokenCredentials();
        Map<String, Object> configuration = Optional.ofNullable(specification.getConfiguration()).orElse(Collections.emptyMap());
        int tokenLength = (int) Optional.ofNullable(configuration.get("length")).orElse(defaultTokenLength);
        credentials.setToken(RandomStringUtils.randomAlphanumeric(tokenLength));
        return TokenCreationResult.builder()
                .credentials(credentials)
                .specification(specification)
                .build();
    }
}
