package com.mmadu.otp.service.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;

@Document
public class DomainOtpConfiguration {
    @Id
    private String id;
    @Indexed(unique = true)
    @NotEmpty(message = "domainId is required")
    private String domainId;
    private String jwkSetUri;
    @Size(min = 1)
    private List<String> supportedProviders = Collections.singletonList("alphanumeric");

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getJwkSetUri() {
        return jwkSetUri;
    }

    public void setJwkSetUri(String jwkSetUri) {
        this.jwkSetUri = jwkSetUri;
    }

    public List<String> getSupportedProviders() {
        return supportedProviders;
    }

    public void setSupportedProviders(List<String> supportedProviders) {
        this.supportedProviders = supportedProviders;
    }
}
