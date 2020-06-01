package com.mmadu.identity.providers.token.creationstrategies;

import com.mmadu.identity.entities.AuthorizationCodeGrantData;
import com.mmadu.identity.entities.GrantAuthorization;
import com.mmadu.identity.exceptions.TokenException;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.TokenRequest;
import com.mmadu.identity.models.token.TokenResponse;
import com.mmadu.identity.models.token.error.InvalidRequest;
import com.mmadu.identity.repositories.GrantAuthorizationRepository;
import com.mmadu.identity.utils.GrantTypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AuthorizationCodeTokenCreationStrategy implements TokenCreationStrategy {
    private GrantAuthorizationRepository grantAuthorizationRepository;

    @Autowired(required = false)
    public void setGrantAuthorizationRepository(GrantAuthorizationRepository grantAuthorizationRepository) {
        this.grantAuthorizationRepository = grantAuthorizationRepository;
    }

    @Override
    public boolean apply(TokenRequest request, MmaduClient client) {
        return GrantTypeUtils.AUTHORIZATION_CODE.equals(request.getGrant_type());
    }

    @Override
    public TokenResponse getToken(TokenRequest request, MmaduClient client) {
        GrantAuthorization authorization = grantAuthorizationRepository.findByAuthorizationCode(request.getCode())
                .orElseThrow(this::invalidCodeError);

        if (!(authorization.getData() instanceof AuthorizationCodeGrantData)) {
            throw invalidGrantData();
        }

        if (authorization.isRedirectUriSpecified() && StringUtils.isEmpty(request.getRedirect_uri())) {
            throw redirectUriIsRequired();
        }

        /*
        TODO:
        1. check if token is still valid
        2. Check if client supports refresh token
        3. Create such property on client and domain configuration
        4. Implement TokenCreatorProvider - JwtToken, Alphanumeric token
        5. Create configuration on domain for accessTokenType and accessTokenProperties, refreshTokenType and refreshTokenProperties
        6. Set domain configuration default to access token -jwt, refresh-token - alphanumeric
        7. Create Access Token
        8. If Configured, create refresh token
        9. Return in response
         */
        return null;
    }

    private TokenException invalidCodeError() {
        return new TokenException(new InvalidRequest("code.invalid", ""));
    }

    private TokenException redirectUriIsRequired() {
        return new TokenException(new InvalidRequest("redirect_uri.required", ""));
    }

    private TokenException invalidGrantData() {
        return new TokenException(new InvalidRequest("grant_data.invalid", ""));
    }
}
