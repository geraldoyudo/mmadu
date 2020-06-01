package com.mmadu.identity.providers.authorization.strategies;

import com.mmadu.identity.entities.AuthorizationCodeGrantData;
import com.mmadu.identity.entities.ClientInstance;
import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.entities.GrantAuthorization;
import com.mmadu.identity.exceptions.ClientInstanceNotFoundException;
import com.mmadu.identity.exceptions.DomainNotFoundException;
import com.mmadu.identity.models.authorization.AuthorizationCodeRedirectData;
import com.mmadu.identity.models.authorization.AuthorizationContext;
import com.mmadu.identity.models.authorization.AuthorizationRequest;
import com.mmadu.identity.models.authorization.AuthorizationResponse;
import com.mmadu.identity.models.users.MmaduUser;
import com.mmadu.identity.providers.authorization.code.DomainAuthorizationCodeGenerator;
import com.mmadu.identity.providers.users.DomainIdentityConfigurationService;
import com.mmadu.identity.repositories.ClientInstanceRepository;
import com.mmadu.identity.repositories.GrantAuthorizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@Order(100)
public class AuthorizationCodeStrategy implements AuthorizationStrategy {
    private static final String RESPONSE_TYPE = "code";

    private ClientInstanceRepository clientInstanceRepository;
    private DomainAuthorizationCodeGenerator authorizationCodeGenerator;
    private GrantAuthorizationRepository grantAuthorizationRepository;
    private DomainIdentityConfigurationService domainIdentityConfigurationService;

    @Autowired
    public void setAuthorizationCodeGenerator(DomainAuthorizationCodeGenerator authorizationCodeGenerator) {
        this.authorizationCodeGenerator = authorizationCodeGenerator;
    }

    @Autowired
    public void setClientInstanceRepository(ClientInstanceRepository clientInstanceRepository) {
        this.clientInstanceRepository = clientInstanceRepository;
    }

    @Autowired
    public void setGrantAuthorizationRepository(GrantAuthorizationRepository grantAuthorizationRepository) {
        this.grantAuthorizationRepository = grantAuthorizationRepository;
    }

    @Autowired
    public void setDomainIdentityConfigurationService(DomainIdentityConfigurationService domainIdentityConfigurationService) {
        this.domainIdentityConfigurationService = domainIdentityConfigurationService;
    }

    @Override
    public boolean apply(AuthorizationRequest request, AuthorizationResponse response) {
        return RESPONSE_TYPE.equals(request.getResponse_type());
    }

    @Override
    public void authorize(AuthorizationRequest request, AuthorizationResponse response, AuthorizationContext context) {
        if (!(context.getAuthorizer() instanceof MmaduUser)) {
            throw new IllegalStateException("invalid authorization");
        }
        MmaduUser authorizer = (MmaduUser) context.getAuthorizer();
        DomainIdentityConfiguration configuration = domainIdentityConfigurationService.findByDomainId(authorizer.getDomainId())
                .orElseThrow(() -> new DomainNotFoundException("domain not found"));
        ClientInstance clientInstance = clientInstanceRepository.findByIdentifier(request.getClient_id())
                .orElseThrow(() -> new ClientInstanceNotFoundException("client instance not found"));
        GrantAuthorization grantAuthorization = new GrantAuthorization();
        grantAuthorization.setClientInstanceId(clientInstance.getId());
        grantAuthorization.setClientId(clientInstance.getClientId());
        grantAuthorization.setDomainId(authorizer.getDomainId());
        grantAuthorization.setUserId(authorizer.getId());
        grantAuthorization.setScopes(response.getScopes());
        AuthorizationCodeGrantData grantData = new AuthorizationCodeGrantData();
        grantData.setCode(authorizationCodeGenerator.generateAuthorizationCodeAsDomain(authorizer.getDomainId()));
        grantData.setCodeExpiryTime(ZonedDateTime.now().plusSeconds(configuration.getGrantCodeTTLSeconds()));
        grantAuthorization.setData(grantData);
        AuthorizationCodeRedirectData data = new AuthorizationCodeRedirectData();
        data.setCode(grantData.getCode());
        data.setState(request.getState());
        grantAuthorizationRepository.save(grantAuthorization);
        context.succeed(data);
    }
}
