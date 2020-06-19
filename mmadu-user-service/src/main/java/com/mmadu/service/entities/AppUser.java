package com.mmadu.service.entities;

import com.mmadu.security.api.DomainPayload;
import com.mmadu.service.models.UserView;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Document
@CompoundIndexes({
        @CompoundIndex(name = "domain_external_id", def = "{'domainId': 1, 'externalId': 1}", unique = true),
        @CompoundIndex(name = "domain_username", def = "{'domainId': 1, 'username': 1}", unique = true)
})
public class AppUser implements DomainPayload {

    @Id
    private String id;
    @NotEmpty
    private String externalId;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    private String domainId;
    private Map<String, Object> properties = new HashMap<>();

    public AppUser() {

    }

    public AppUser(String domainId, UserView userView) {
        this.externalId = userView.getId();
        this.domainId = domainId;
        this.username = userView.getUsername();
        this.password = userView.getPassword();
        this.properties = new HashMap<>(userView.getProperties());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Optional get(String property) {
        return Optional.ofNullable(properties.get(property));
    }


    public void set(String property, Object value) {
        properties.put(property, value);
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public boolean passwordMatches(String password) {
        return this.password.equals(password);
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public UserView userView() {
        return new UserView(
                externalId,
                username,
                password,
                Collections.emptyList(),
                Collections.emptyList(),
                new HashMap<>(properties)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AppUser appUser = (AppUser) o;

        return new EqualsBuilder()
                .append(id, appUser.id)
                .append(externalId, appUser.externalId)
                .append(username, appUser.username)
                .append(password, appUser.password)
                .append(domainId, appUser.domainId)
                .append(properties, appUser.properties)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(externalId)
                .append(username)
                .append(password)
                .append(domainId)
                .append(properties)
                .toHashCode();
    }
}
