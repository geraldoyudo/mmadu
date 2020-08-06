package com.mmadu.identity.entities;

import com.mmadu.identity.models.themes.ThemeConfiguration;
import com.mmadu.identity.providers.authorization.code.AlphaNumericCodeGenerator;
import com.mmadu.security.api.DomainPayload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

@Data
@Document
@EqualsAndHashCode
public class DomainIdentityConfiguration implements DomainPayload {
    @Id
    private String id;
    @Indexed(unique = true)
    @NotEmpty(message = "domainId is required")
    private String domainId;
    @NotEmpty(message = "authorizationCodeType is required")
    private String authorizationCodeType = AlphaNumericCodeGenerator.TYPE;
    private Map<String, Object> authorizationCodeTypeProperties = new HashMap<>();
    private Long authorizationCodeTTLSeconds = 600L;
    private Long maxAuthorizationTTLSeconds = 24 * 60 * 60L;
    private boolean refreshTokenEnabled = true;
    @NotEmpty(message = "accessTokenProvider is required")
    private String accessTokenProvider = "jwt";
    private Map<String, Object> accessTokenProperties = new HashMap<>();
    @NotEmpty(message = "refreshTokenProvider is required")
    private String refreshTokenProvider = "alphanumeric";
    private Map<String, Object> refreshTokenProperties = new HashMap<>();
    private String issuerId;
    private ThemeConfiguration themeConfiguration = new ThemeConfiguration();
}
