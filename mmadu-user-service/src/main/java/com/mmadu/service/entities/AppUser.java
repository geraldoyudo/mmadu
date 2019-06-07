package com.mmadu.service.entities;

import com.mmadu.service.model.UserView;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.*;

@Document
public class AppUser {

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
    private Collection<String> roles = new HashSet<>();
    private Collection<String> authorities = new HashSet<>();
    private Map<String, Object> properties = new HashMap<>();

    public AppUser() {

    }

    public AppUser(String domainId, UserView userView) {
        this.externalId = userView.getId();
        this.domainId = domainId;
        this.username = userView.getUsername();
        this.password = userView.getPassword();
        this.roles = new ArrayList<>(userView.getRoles());
        this.authorities = new ArrayList<>(userView.getAuthorities());
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

    public Collection<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Collection<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
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

    public void addRoles(String... rolesArray) {
        roles.addAll(Arrays.asList(rolesArray));
    }

    public void addAuthorities(String... authoritiesArray) {
        authorities.addAll(Arrays.asList(authoritiesArray));
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
                new ArrayList<>(roles),
                new ArrayList<>(authorities),
                new HashMap<>(properties)
        );
    }
}
