package com.mmadu.identity.entities;

import com.mmadu.identity.providers.authorization.code.AlphaNumericCodeGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Data
@Document
@EqualsAndHashCode
public class DomainIdentityConfiguration implements HasDomain {
    @Id
    private String id;
    @Indexed(unique = true)
    private String domainId;
    private String grantCodeType = AlphaNumericCodeGenerator.TYPE;
    private Map<String, Object> grantCodeTypeProperties;
    private Long grantCodeTTLSeconds = 600L;
    private Long maxAuthorizationTTLSeconds = 24 * 60 * 60L;
    private boolean refreshTokenEnabled = true;
    private String accessTokenProvider = "jwt";
    private Map<String, Object> accessTokenProperties = new HashMap<>();
    private String refreshTokenProvider = "alphanumeric";
    private Map<String, Object> refreshTokenProperties = new HashMap<>();
    private String issuerId;
}
