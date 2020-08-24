package com.mmadu.identity.config;

import com.mmadu.identity.entities.*;
import com.mmadu.identity.models.authorization.AuthorizationProfile;
import com.mmadu.identity.models.security.CredentialGenerationRequest;
import com.mmadu.identity.models.signin.SignInProfile;
import com.mmadu.identity.models.themes.ThemeConfiguration;
import com.mmadu.identity.providers.authorization.code.AlphaNumericCodeGenerator;
import com.mmadu.identity.utils.ClientCategoryUtils;
import com.mmadu.identity.utils.ClientProfileUtils;
import com.mmadu.identity.utils.GrantTypeUtils;
import com.mmadu.identity.utils.TokenCategoryUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Component
@ConfigurationProperties(prefix = "mmadu.domain-identity-config")
public class DomainIdentityConfigurationList {
    private List<DomainIdentityItem> domains = Collections.emptyList();

    public List<DomainIdentityItem> getDomains() {
        return domains;
    }

    public void setDomains(List<DomainIdentityItem> domains) {
        this.domains = domains;
    }

    public static class DomainIdentityItem {
        @NotEmpty
        private String domainId;
        @NotEmpty
        private String authorizationCodeType = AlphaNumericCodeGenerator.TYPE;
        private Map<String, Object> authorizationCodeTypeProperties = Collections.emptyMap();
        private Long authorizationCodeTTLSeconds = 600L;
        private Long maxAuthorizationTTLSeconds = 24 * 60 * 60L;
        private boolean refreshTokenEnabled = true;
        @NotEmpty
        private String accessTokenProvider = "jwt";
        private Map<String, Object> accessTokenProperties = new HashMap<>();
        @NotEmpty
        private String refreshTokenProvider = "alphanumeric";
        private Map<String, Object> refreshTokenProperties = new HashMap<>();
        @NotEmpty
        private String issuerId;
        private SignInProfile defaultLoginProfile = new SignInProfile();
        private Map<String, SignInProfile> loginProfiles = new HashMap<>();
        @NotNull
        private ThemeConfiguration theme = new ThemeConfiguration();
        private List<ClientItem> clients = Collections.emptyList();
        private List<ClientInstanceItem> clientInstances = Collections.emptyList();
        private List<ResourceItem> resources = Collections.emptyList();
        private List<ScopeItem> scopes = Collections.emptyList();

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

        public List<ClientItem> getClients() {
            return clients;
        }

        public void setClients(List<ClientItem> clients) {
            this.clients = clients;
        }

        public List<ClientInstanceItem> getClientInstances() {
            return clientInstances;
        }

        public void setClientInstances(List<ClientInstanceItem> clientInstances) {
            this.clientInstances = clientInstances;
        }

        public List<ResourceItem> getResources() {
            return resources;
        }

        public void setResources(List<ResourceItem> resources) {
            this.resources = resources;
        }

        public List<ScopeItem> getScopes() {
            return scopes;
        }

        public void setScopes(List<ScopeItem> scopes) {
            this.scopes = scopes;
        }

        public SignInProfile getDefaultLoginProfile() {
            return defaultLoginProfile;
        }

        public void setDefaultLoginProfile(SignInProfile defaultLoginProfile) {
            this.defaultLoginProfile = defaultLoginProfile;
        }

        public Map<String, SignInProfile> getLoginProfiles() {
            return loginProfiles;
        }

        public void setLoginProfiles(Map<String, SignInProfile> loginProfiles) {
            this.loginProfiles = loginProfiles;
        }

        public DomainIdentityConfiguration toEntity() {
            DomainIdentityConfiguration configuration = new DomainIdentityConfiguration();
            configuration.setDomainId(domainId);
            configuration.setAuthorizationCodeType(authorizationCodeType);
            configuration.setAuthorizationCodeTypeProperties(authorizationCodeTypeProperties);
            configuration.setAuthorizationCodeTTLSeconds(authorizationCodeTTLSeconds);
            configuration.setMaxAuthorizationTTLSeconds(maxAuthorizationTTLSeconds);
            configuration.setAccessTokenProperties(accessTokenProperties);
            configuration.setAccessTokenProvider(accessTokenProvider);
            configuration.setRefreshTokenEnabled(refreshTokenEnabled);
            configuration.setRefreshTokenProvider(refreshTokenProvider);
            configuration.setRefreshTokenProperties(refreshTokenProperties);
            configuration.setIssuerId(issuerId);
            configuration.setTheme(theme);
            configuration.setDefaultLoginProfile(defaultLoginProfile);
            configuration.setLoginProfiles(loginProfiles);
            return configuration;
        }


    }

    public static class CredentialItem {
        @NotEmpty
        private String type;
        private Map<String, Object> properties = Collections.emptyMap();

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Map<String, Object> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

        public CredentialGenerationRequest toRequest() {
            return new CredentialGenerationRequest(type, properties);
        }
    }

    public static class ClientItem {
        @NotEmpty
        private String name;
        @NotEmpty
        private String code = UUID.randomUUID().toString();
        @NotEmpty
        private String applicationUrl;
        private String logoUrl;
        private String category = ClientCategoryUtils.FIRST_PARTY_APP;
        private List<String> tags = Collections.emptyList();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getApplicationUrl() {
            return applicationUrl;
        }

        public void setApplicationUrl(String applicationUrl) {
            this.applicationUrl = applicationUrl;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public Client toEntity(String domainId) {
            Client client = new Client();
            client.setApplicationUrl(applicationUrl);
            client.setTags(tags);
            client.setCategory(category);
            client.setCode(code);
            client.setLogoUrl(logoUrl);
            client.setName(name);
            client.setDomainId(domainId);
            return client;
        }
    }

    public static class ClientInstanceItem {
        @NotEmpty
        private String clientCode;
        private ClientType clientType = ClientType.CONFIDENTIAL;
        @NotEmpty
        private String clientProfile = ClientProfileUtils.WEB_APP;
        private Map<String, Object> credentials;
        @NotEmpty
        private String identifier;
        private List<String> redirectionUris = Collections.emptyList();
        private List<String> allowedHosts = Collections.emptyList();
        private boolean tlsEnabled = true;
        private List<String> supportedGrantTypes = Collections.singletonList(GrantTypeUtils.AUTHORIZATION_CODE);
        @NotNull
        @Size(min = 1)
        private List<String> resources = Collections.emptyList();
        private List<String> authorities = Collections.emptyList();
        private boolean issueRefreshTokens = true;
        private Long authorizationCodeGrantTypeTTLSeconds = 24 * 60 * 60l;
        private Long implicitGrantTypeTTLSeconds = 60 * 60L;
        private Long accessTokenTTLSeconds = 300L;
        private Long refreshTokenTTLSeconds = 60 * 60L;
        private Long clientCredentialsGrantTypeTTLSeconds = 24 * 60 * 60l;
        private boolean includeUserRoles;
        private boolean includeUserAuthorities;
        private boolean includeUserGroups;
        private List<String> scopes = Collections.emptyList();
        private String tokenCategory = TokenCategoryUtils.CATEGORY_BEARER;
        private AuthorizationProfile authorizationProfile = new AuthorizationProfile();

        public String getClientCode() {
            return clientCode;
        }

        public void setClientCode(String clientCode) {
            this.clientCode = clientCode;
        }

        public ClientType getClientType() {
            return clientType;
        }

        public void setClientType(ClientType clientType) {
            this.clientType = clientType;
        }

        public String getClientProfile() {
            return clientProfile;
        }

        public void setClientProfile(String clientProfile) {
            this.clientProfile = clientProfile;
        }

        public Map<String, Object> getCredentials() {
            return credentials;
        }

        public void setCredentials(Map<String, Object> credentials) {
            this.credentials = credentials;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public List<String> getRedirectionUris() {
            return redirectionUris;
        }

        public void setRedirectionUris(List<String> redirectionUris) {
            this.redirectionUris = redirectionUris;
        }

        public List<String> getAllowedHosts() {
            return allowedHosts;
        }

        public void setAllowedHosts(List<String> allowedHosts) {
            this.allowedHosts = allowedHosts;
        }

        public boolean isTlsEnabled() {
            return tlsEnabled;
        }

        public void setTlsEnabled(boolean tlsEnabled) {
            this.tlsEnabled = tlsEnabled;
        }

        public List<String> getSupportedGrantTypes() {
            return supportedGrantTypes;
        }

        public void setSupportedGrantTypes(List<String> supportedGrantTypes) {
            this.supportedGrantTypes = supportedGrantTypes;
        }

        public List<String> getResources() {
            return resources;
        }

        public void setResources(List<String> resources) {
            this.resources = resources;
        }

        public List<String> getAuthorities() {
            return authorities;
        }

        public void setAuthorities(List<String> authorities) {
            this.authorities = authorities;
        }

        public boolean isIssueRefreshTokens() {
            return issueRefreshTokens;
        }

        public void setIssueRefreshTokens(boolean issueRefreshTokens) {
            this.issueRefreshTokens = issueRefreshTokens;
        }

        public Long getAuthorizationCodeGrantTypeTTLSeconds() {
            return authorizationCodeGrantTypeTTLSeconds;
        }

        public void setAuthorizationCodeGrantTypeTTLSeconds(Long authorizationCodeGrantTypeTTLSeconds) {
            this.authorizationCodeGrantTypeTTLSeconds = authorizationCodeGrantTypeTTLSeconds;
        }

        public Long getImplicitGrantTypeTTLSeconds() {
            return implicitGrantTypeTTLSeconds;
        }

        public void setImplicitGrantTypeTTLSeconds(Long implicitGrantTypeTTLSeconds) {
            this.implicitGrantTypeTTLSeconds = implicitGrantTypeTTLSeconds;
        }

        public Long getAccessTokenTTLSeconds() {
            return accessTokenTTLSeconds;
        }

        public void setAccessTokenTTLSeconds(Long accessTokenTTLSeconds) {
            this.accessTokenTTLSeconds = accessTokenTTLSeconds;
        }

        public Long getRefreshTokenTTLSeconds() {
            return refreshTokenTTLSeconds;
        }

        public void setRefreshTokenTTLSeconds(Long refreshTokenTTLSeconds) {
            this.refreshTokenTTLSeconds = refreshTokenTTLSeconds;
        }

        public Long getClientCredentialsGrantTypeTTLSeconds() {
            return clientCredentialsGrantTypeTTLSeconds;
        }

        public void setClientCredentialsGrantTypeTTLSeconds(Long clientCredentialsGrantTypeTTLSeconds) {
            this.clientCredentialsGrantTypeTTLSeconds = clientCredentialsGrantTypeTTLSeconds;
        }

        public boolean isIncludeUserRoles() {
            return includeUserRoles;
        }

        public void setIncludeUserRoles(boolean includeUserRoles) {
            this.includeUserRoles = includeUserRoles;
        }

        public boolean isIncludeUserAuthorities() {
            return includeUserAuthorities;
        }

        public void setIncludeUserAuthorities(boolean includeUserAuthorities) {
            this.includeUserAuthorities = includeUserAuthorities;
        }

        public boolean isIncludeUserGroups() {
            return includeUserGroups;
        }

        public void setIncludeUserGroups(boolean includeUserGroups) {
            this.includeUserGroups = includeUserGroups;
        }

        public List<String> getScopes() {
            return scopes;
        }

        public void setScopes(List<String> scopes) {
            this.scopes = scopes;
        }

        public String getTokenCategory() {
            return tokenCategory;
        }

        public void setTokenCategory(String tokenCategory) {
            this.tokenCategory = tokenCategory;
        }

        public AuthorizationProfile getAuthorizationProfile() {
            return authorizationProfile;
        }

        public void setAuthorizationProfile(AuthorizationProfile authorizationProfile) {
            this.authorizationProfile = authorizationProfile;
        }

        public ClientInstance toEntity(String domainId,
                                       ClientResolver clientResolver,
                                       CredentialConverter credentialConverter) {
            ClientInstance instance = new ClientInstance();
            instance.setClientId(
                    clientResolver.getClient(clientCode).orElseThrow(() ->
                            new IllegalStateException("client not found: " + clientCode)).getId()
            );
            instance.setClientType(clientType);
            instance.setClientProfile(clientProfile);
            instance.setCredentials(credentialConverter.convert(credentials));
            instance.setIdentifier(identifier);
            instance.setRedirectionUris(redirectionUris);
            instance.setAllowedHosts(allowedHosts);
            instance.setTlsEnabled(tlsEnabled);
            instance.setSupportedGrantTypes(supportedGrantTypes);
            instance.setResources(resources);
            instance.setAuthorities(authorities);
            instance.setIssueRefreshTokens(issueRefreshTokens);
            instance.setAuthorizationCodeGrantTypeTTLSeconds(authorizationCodeGrantTypeTTLSeconds);
            instance.setImplicitGrantTypeTTLSeconds(implicitGrantTypeTTLSeconds);
            instance.setAccessTokenTTLSeconds(accessTokenTTLSeconds);
            instance.setRefreshTokenTTLSeconds(refreshTokenTTLSeconds);
            instance.setClientCredentialsGrantTypeTTLSeconds(clientCredentialsGrantTypeTTLSeconds);
            instance.setIncludeUserRoles(includeUserRoles);
            instance.setIncludeUserAuthorities(includeUserAuthorities);
            instance.setIncludeUserGroups(includeUserGroups);
            instance.setScopes(scopes);
            instance.setDomainId(domainId);
            instance.setTokenCategory(tokenCategory);
            instance.setAuthorizationProfile(authorizationProfile);
            return instance;
        }
    }

    @FunctionalInterface
    public interface ClientResolver {
        Optional<Client> getClient(String code);
    }

    @FunctionalInterface
    public interface CredentialConverter {
        ClientCredentials convert(Map<String, Object> credentials);
    }

    public static class ResourceItem {
        @NotEmpty
        private String identifier;
        @NotEmpty
        private String name;
        @NotEmpty
        private String description;
        @NotNull
        @Size(min = 1)
        private List<String> supportedTokenCategories = Collections.singletonList(TokenCategoryUtils.CATEGORY_BEARER);

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<String> getSupportedTokenCategories() {
            return supportedTokenCategories;
        }

        public void setSupportedTokenCategories(List<String> supportedTokenCategories) {
            this.supportedTokenCategories = supportedTokenCategories;
        }

        public Resource toEntity(String domainId) {
            Resource resource = new Resource();
            resource.setDomainId(domainId);
            resource.setDescription(description);
            resource.setIdentifier(identifier);
            resource.setName(name);
            resource.setSupportedTokenCategories(supportedTokenCategories);
            return resource;
        }
    }

    public static class ScopeItem {
        @NotEmpty
        private String code;
        @NotEmpty
        private String name;
        @NotEmpty
        private String description;
        @NotEmpty
        private List<String> authorities = Collections.emptyList();

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<String> getAuthorities() {
            return authorities;
        }

        public void setAuthorities(List<String> authorities) {
            this.authorities = authorities;
        }

        public Scope toEntity(String domainId) {
            Scope scope = new Scope();
            scope.setCode(code);
            scope.setAuthorities(authorities);
            scope.setDescription(description);
            scope.setDomainId(domainId);
            scope.setName(name);
            return scope;
        }
    }
}
