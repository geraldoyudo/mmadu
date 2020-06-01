package com.mmadu.identity.entities;

import com.mmadu.identity.providers.authorization.code.AlphaNumericCodeGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private boolean refreshTokenEnabled = true;
}
