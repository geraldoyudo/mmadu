package com.mmadu.identity.providers.authorization.strategies;

import com.mmadu.identity.entities.AuthorizationCodeGrantData;
import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.entities.GrantAuthorization;
import com.mmadu.identity.models.authorization.AuthorizationCodeRedirectData;
import com.mmadu.identity.models.authorization.AuthorizationContext;
import com.mmadu.identity.models.authorization.AuthorizationRequest;
import com.mmadu.identity.models.authorization.AuthorizationResponse;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.user.MmaduUser;
import com.mmadu.identity.providers.authorization.code.DomainAuthorizationCodeGenerator;
import com.mmadu.identity.repositories.GrantAuthorizationRepository;
import com.mmadu.identity.utils.AuthorizationUtils;
import com.mmadu.identity.utils.GrantTypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.ObjectUtils.min;

@Component
@Order(100)
public class AuthorizationCodeStrategy implements AuthorizationStrategy {
    private static final String RESPONSE_TYPE = "code";

    private DomainAuthorizationCodeGenerator authorizationCodeGenerator;
    private GrantAuthorizationRepository grantAuthorizationRepository;

    @Autowired
    public void setAuthorizationCodeGenerator(DomainAuthorizationCodeGenerator authorizationCodeGenerator) {
        this.authorizationCodeGenerator = authorizationCodeGenerator;
    }

    @Autowired
    public void setGrantAuthorizationRepository(GrantAuthorizationRepository grantAuthorizationRepository) {
        this.grantAuthorizationRepository = grantAuthorizationRepository;
    }

    @Override
    public boolean apply(AuthorizationRequest request, AuthorizationResponse response, AuthorizationContext context) {
        return RESPONSE_TYPE.equals(request.getResponse_type()) &&
                Optional.ofNullable(context.getClient().getSupportedGrantTypes())
                        .orElse(emptyList())
                        .contains(GrantTypeUtils.AUTHORIZATION_CODE);
    }

    @Override
    public void authorize(AuthorizationRequest request, AuthorizationResponse response, AuthorizationContext context) {
        AuthorizationUtils.ensureMmaduUserAuthorizer(context);
        MmaduUser authorizer = (MmaduUser) context.getAuthorizer();
        DomainIdentityConfiguration configuration = context.getDomainConfiguration();
        MmaduClient client = context.getClient();
        GrantAuthorization grantAuthorization = new GrantAuthorization();
        grantAuthorization.setClientInstanceId(client.getInstanceId());
        grantAuthorization.setClientId(client.getId());
        grantAuthorization.setDomainId(authorizer.getDomainId());
        grantAuthorization.setUserId(authorizer.getId());
        grantAuthorization.setUsername(authorizer.getUsername());
        grantAuthorization.setUserRoles(authorizer.getRoles());
        grantAuthorization.setUserGroups(authorizer.getGroups());
        grantAuthorization.setUserAuthorities(authorizer.getAuthorities());
        grantAuthorization.setScopes(response.getScopes());
        grantAuthorization.setRedirectUri(context.getResult().getRedirectUri());
        grantAuthorization.setRedirectUriSpecified(context.getResult().isRedirectUriSpecified());
        grantAuthorization.setClientIdentifier(client.getClientIdentifier());
        grantAuthorization.setGrantType(GrantTypeUtils.AUTHORIZATION_CODE);
        grantAuthorization.setState(request.getState());
        ZonedDateTime now = ZonedDateTime.now();
        grantAuthorization.setIssuedTime(now);
        grantAuthorization.setExpiryTime(
                now.plusSeconds(min(configuration.getMaxAuthorizationTTLSeconds(), client.getAuthorizationCodeGrantTypeTTLSeconds()))
        );
        AuthorizationCodeGrantData grantData = new AuthorizationCodeGrantData();
        grantData.setCode(authorizationCodeGenerator.generateAuthorizationCodeAsDomain(authorizer.getDomainId()));
        grantData.setCodeExpiryTime(ZonedDateTime.now().plusSeconds(configuration.getAuthorizationCodeTTLSeconds()));
        grantAuthorization.setData(grantData);
        AuthorizationCodeRedirectData data = new AuthorizationCodeRedirectData();
        data.setCode(grantData.getCode());
        data.setState(request.getState());
        grantAuthorizationRepository.save(grantAuthorization);
        context.succeed(data);
    }

    @Override
    public boolean isGrantType() {
        return true;
    }
}
