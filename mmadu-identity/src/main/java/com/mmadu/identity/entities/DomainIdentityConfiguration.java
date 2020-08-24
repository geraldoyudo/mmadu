package com.mmadu.identity.entities;

import com.mmadu.identity.models.signin.SignInProfile;
import com.mmadu.identity.models.themes.ThemeConfiguration;
import com.mmadu.identity.providers.authorization.code.AlphaNumericCodeGenerator;
import com.mmadu.security.api.DomainPayload;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

@Document
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
    private ThemeConfiguration theme = new ThemeConfiguration();
    private Map<String, SignInProfile> loginProfiles = new HashMap<>();
    private SignInProfile defaultLoginProfile = new SignInProfile();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getAuthorizationCodeType() {
        return authorizationCodeType;
    }

    public void setAuthorizationCodeType(String authorizationCodeType) {
        this.authorizationCodeType = authorizationCodeType;
    }

    public Map<String, Object> getAuthorizationCodeTypeProperties() {
        return authorizationCodeTypeProperties;
    }

    public void setAuthorizationCodeTypeProperties(Map<String, Object> authorizationCodeTypeProperties) {
        this.authorizationCodeTypeProperties = authorizationCodeTypeProperties;
    }

    public Long getAuthorizationCodeTTLSeconds() {
        return authorizationCodeTTLSeconds;
    }

    public void setAuthorizationCodeTTLSeconds(Long authorizationCodeTTLSeconds) {
        this.authorizationCodeTTLSeconds = authorizationCodeTTLSeconds;
    }

    public Long getMaxAuthorizationTTLSeconds() {
        return maxAuthorizationTTLSeconds;
    }

    public void setMaxAuthorizationTTLSeconds(Long maxAuthorizationTTLSeconds) {
        this.maxAuthorizationTTLSeconds = maxAuthorizationTTLSeconds;
    }

    public boolean isRefreshTokenEnabled() {
        return refreshTokenEnabled;
    }

    public void setRefreshTokenEnabled(boolean refreshTokenEnabled) {
        this.refreshTokenEnabled = refreshTokenEnabled;
    }

    public String getAccessTokenProvider() {
        return accessTokenProvider;
    }

    public void setAccessTokenProvider(String accessTokenProvider) {
        this.accessTokenProvider = accessTokenProvider;
    }

    public Map<String, Object> getAccessTokenProperties() {
        return accessTokenProperties;
    }

    public void setAccessTokenProperties(Map<String, Object> accessTokenProperties) {
        this.accessTokenProperties = accessTokenProperties;
    }

    public String getRefreshTokenProvider() {
        return refreshTokenProvider;
    }

    public void setRefreshTokenProvider(String refreshTokenProvider) {
        this.refreshTokenProvider = refreshTokenProvider;
    }

    public Map<String, Object> getRefreshTokenProperties() {
        return refreshTokenProperties;
    }

    public void setRefreshTokenProperties(Map<String, Object> refreshTokenProperties) {
        this.refreshTokenProperties = refreshTokenProperties;
    }

    public String getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(String issuerId) {
        this.issuerId = issuerId;
    }

    public ThemeConfiguration getTheme() {
        return theme;
    }

    public void setTheme(ThemeConfiguration theme) {
        this.theme = theme;
    }

    public Map<String, SignInProfile> getLoginProfiles() {
        return loginProfiles;
    }

    public void setLoginProfiles(Map<String, SignInProfile> loginProfiles) {
        this.loginProfiles = loginProfiles;
    }

    public SignInProfile getDefaultLoginProfile() {
        return defaultLoginProfile;
    }

    public void setDefaultLoginProfile(SignInProfile defaultLoginProfile) {
        this.defaultLoginProfile = defaultLoginProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DomainIdentityConfiguration that = (DomainIdentityConfiguration) o;

        return new EqualsBuilder()
                .append(refreshTokenEnabled, that.refreshTokenEnabled)
                .append(id, that.id)
                .append(domainId, that.domainId)
                .append(authorizationCodeType, that.authorizationCodeType)
                .append(authorizationCodeTypeProperties, that.authorizationCodeTypeProperties)
                .append(authorizationCodeTTLSeconds, that.authorizationCodeTTLSeconds)
                .append(maxAuthorizationTTLSeconds, that.maxAuthorizationTTLSeconds)
                .append(accessTokenProvider, that.accessTokenProvider)
                .append(accessTokenProperties, that.accessTokenProperties)
                .append(refreshTokenProvider, that.refreshTokenProvider)
                .append(refreshTokenProperties, that.refreshTokenProperties)
                .append(issuerId, that.issuerId)
                .append(theme, that.theme)
                .append(loginProfiles, that.loginProfiles)
                .append(defaultLoginProfile, that.defaultLoginProfile)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(domainId)
                .append(authorizationCodeType)
                .append(authorizationCodeTypeProperties)
                .append(authorizationCodeTTLSeconds)
                .append(maxAuthorizationTTLSeconds)
                .append(refreshTokenEnabled)
                .append(accessTokenProvider)
                .append(accessTokenProperties)
                .append(refreshTokenProvider)
                .append(refreshTokenProperties)
                .append(issuerId)
                .append(theme)
                .append(loginProfiles)
                .append(defaultLoginProfile)
                .toHashCode();
    }
}
