package com.mmadu.notifications.service.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Document
public class DomainNotificationConfiguration {
    @Id
    private String id;
    @Indexed(unique = true)
    @NotEmpty(message = "domainId is required")
    private String domainId;
    private String jwkSetUri;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DomainNotificationConfiguration that = (DomainNotificationConfiguration) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(domainId, that.domainId)
                .append(jwkSetUri, that.jwkSetUri)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(domainId)
                .append(jwkSetUri)
                .toHashCode();
    }
}
