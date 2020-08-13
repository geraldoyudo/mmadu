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
    private Map<String, Boolean> propertyValidationState = new HashMap<>();
    private boolean credentialExpired = false;
    private boolean enabled = true;
    private boolean locked = false;
    private boolean active = true;

    public AppUser() {

    }

    public AppUser(String domainId, UserView userView) {
        this.externalId = userView.getId();
        this.domainId = domainId;
        this.username = userView.getUsername();
        this.password = userView.getPassword();
        this.properties = new HashMap<>(userView.getProperties());
        this.active = userView.isActive();
        this.credentialExpired = userView.isCredentialExpired();
        this.enabled = userView.isEnabled();
        this.locked = userView.isLocked();
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

    public Map<String, Boolean> getPropertyValidationState() {
        return propertyValidationState;
    }

    public void setPropertyValidationState(Map<String, Boolean> propertyValidationState) {
        this.propertyValidationState = propertyValidationState;
    }

    public void addPropertyValidationStateEntry(String property, boolean state) {
        this.propertyValidationState.put(property, state);
    }

    public UserView userView() {
        UserView view = new UserView(
                externalId,
                username,
                password,
                Collections.emptyList(),
                Collections.emptyList(),
                new HashMap<>(properties)
        );
        view.setPropertyValidationState(propertyValidationState);
        return view;
    }

    public boolean isCredentialExpired() {
        return credentialExpired;
    }

    public void setCredentialExpired(boolean credentialExpired) {
        this.credentialExpired = credentialExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AppUser user = (AppUser) o;

        return new EqualsBuilder()
                .append(id, user.id)
                .append(externalId, user.externalId)
                .append(username, user.username)
                .append(password, user.password)
                .append(domainId, user.domainId)
                .append(properties, user.properties)
                .append(propertyValidationState, user.propertyValidationState)
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
                .append(propertyValidationState)
                .toHashCode();
    }
}
