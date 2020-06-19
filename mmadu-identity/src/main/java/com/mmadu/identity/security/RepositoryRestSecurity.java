package com.mmadu.identity.security;

import com.mmadu.identity.entities.*;
import com.mmadu.identity.entities.credentials.Credential;
import com.mmadu.identity.entities.keys.Key;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RepositoryRestSecurity {

    @HandleBeforeCreate
    @PreAuthorize("hasAuthority('credential.create')")
    public void createCredential(@P("credential") Credential credential) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('credential.update')")
    public void updateCredential(@P("credential") Credential credential) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasAuthority('credential.delete')")
    public void deleteCredential(@P("credential") Credential credential) {

    }

    @HandleBeforeCreate
    @PreAuthorize("hasAuthority('client.create')")
    public void createClient(@P("client") Client client) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('client.update')")
    public void updateClient(@P("client") Client client) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasAuthority('client.delete')")
    public void deleteClient(@P("client") Client client) {

    }

    @HandleBeforeCreate
    @PreAuthorize("hasAuthority('client_instance.create')")
    public void createClientInstance(@P("clientInstance") ClientInstance clientInstance) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('client_instance.update')")
    public void updateClientInstance(@P("clientInstance") ClientInstance clientInstance) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasAuthority('client_instance.delete')")
    public void deleteClientInstance(@P("clientInstance") ClientInstance clientInstance) {

    }

    @HandleBeforeCreate
    @PreAuthorize("hasAuthority('identity_config.create')")
    public void createDomainIdentityConfiguration(@P("domainIdentityConfiguration")
                                                          DomainIdentityConfiguration domainIdentityConfiguration) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('identity_config.update')")
    public void updateDomainIdentityConfiguration(@P("domainIdentityConfiguration")
                                                          DomainIdentityConfiguration domainIdentityConfiguration) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasAuthority('identity_config.delete')")
    public void deleteDomainIdentityConfiguration(@P("domainIdentityConfiguration")
                                                          DomainIdentityConfiguration domainIdentityConfiguration) {

    }

    @HandleBeforeCreate
    @PreAuthorize("hasAuthority('grant_authorization.create')")
    public void createGrantAuthorization(@P("grantAuthorization") GrantAuthorization grantAuthorization) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('grant_authorization.update')")
    public void updateGrantAuthorization(@P("grantAuthorization") GrantAuthorization grantAuthorization) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasAuthority('grant_authorization.delete')")
    public void deleteGrantAuthorization(@P("grantAuthorization") GrantAuthorization grantAuthorization) {

    }

    @HandleBeforeCreate
    @PreAuthorize("hasAuthority('resource.create')")
    public void createResource(@P("resource") Resource resource) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('resource.update')")
    public void updateResource(@P("resource") Resource resource) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasAuthority('resource.delete')")
    public void deleteResource(@P("resource") Resource resource) {

    }

    @HandleBeforeCreate
    @PreAuthorize("hasAuthority('scope.create')")
    public void createScope(@P("scope") Scope scope) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('scope.update')")
    public void updateScope(@P("scope") Scope scope) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasAuthority('scope.delete')")
    public void deleteScope(@P("scope") Scope scope) {

    }

    @HandleBeforeCreate
    @PreAuthorize("hasAuthority('token.create')")
    public void createToken(@P("token") Token token) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('token.update')")
    public void updateToken(@P("token") Token token) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasAuthority('token.delete')")
    public void deleteToken(@P("token") Token token) {

    }

    @HandleBeforeCreate
    @PreAuthorize("hasAuthority('key.create')")
    public void createKey(@P("key") Key key) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('key.update')")
    public void updateKey(@P("key") Key key) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasAuthority('key.delete')")
    public void deleteKey(@P("key") Key key) {

    }
}
