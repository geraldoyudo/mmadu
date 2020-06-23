package com.mmadu.identity.config;

import com.mmadu.identity.entities.*;
import com.mmadu.identity.models.security.CredentialGenerationRequest;
import com.mmadu.identity.providers.authorization.code.AlphaNumericCodeGenerator;
import com.mmadu.identity.utils.ClientCategoryUtils;
import com.mmadu.identity.utils.ClientProfileUtils;
import com.mmadu.identity.utils.GrantTypeUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Component
@ConfigurationProperties(prefix = "mmadu.domain-identity-config")
@Data
public class DomainIdentityConfigurationList {
    private List<DomainIdentityItem> domains = Collections.emptyList();

    @Data
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
        private List<ClientItem> clients = Collections.emptyList();
        private List<ClientInstanceItem> clientInstances = Collections.emptyList();
        private List<ResourceItem> resources = Collections.emptyList();
        private List<ScopeItem> scopes = Collections.emptyList();

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
            return configuration;
        }


    }

    @Data
    public static class CredentialItem {
        @NotEmpty
        private String type;
        private Map<String, Object> properties = Collections.emptyMap();

        public CredentialGenerationRequest toRequest() {
            return new CredentialGenerationRequest(type, properties);
        }
    }

    @Data
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

    @Data
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
        private Long accessTokenTTLSeconds = 300L;
        private Long refreshTokenTTLSeconds = 60 * 60L;
        private Long clientCredentialsGrantTypeTTLSeconds = 24 * 60 * 60l;
        private boolean includeUserRoles;
        private boolean includeUserAuthorities;
        private boolean includeUserGroups;
        private List<String> scopes = Collections.emptyList();

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
            instance.setAccessTokenTTLSeconds(accessTokenTTLSeconds);
            instance.setRefreshTokenTTLSeconds(refreshTokenTTLSeconds);
            instance.setClientCredentialsGrantTypeTTLSeconds(clientCredentialsGrantTypeTTLSeconds);
            instance.setIncludeUserRoles(includeUserRoles);
            instance.setIncludeUserAuthorities(includeUserAuthorities);
            instance.setIncludeUserGroups(includeUserGroups);
            instance.setScopes(scopes);
            instance.setDomainId(domainId);
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

    @Data
    public static class ResourceItem {
        @NotEmpty
        private String identifier;
        @NotEmpty
        private String name;
        @NotEmpty
        private String description;

        public Resource toEntity(String domainId) {
            Resource resource = new Resource();
            resource.setDomainId(domainId);
            resource.setDescription(description);
            resource.setIdentifier(identifier);
            resource.setName(name);
            return resource;
        }
    }

    @Data
    public static class ScopeItem {
        @NotEmpty
        private String code;
        @NotEmpty
        private String name;
        @NotEmpty
        private String description;
        @NotEmpty
        private List<String> authorities = Collections.emptyList();

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
