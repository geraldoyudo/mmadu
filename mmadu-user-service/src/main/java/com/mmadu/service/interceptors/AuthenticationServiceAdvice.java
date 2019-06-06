package com.mmadu.service.interceptors;

import static com.mmadu.service.utilities.DomainAuthenticationConstants.DOMAIN_AUTH_TOKEN_FIELD;

import com.mmadu.service.models.AuthenticateRequest;
import com.mmadu.service.providers.DomainTokenAuthenticator;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AuthenticationServiceAdvice {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    @Qualifier("authenticateApi")
    private DomainTokenAuthenticator apiAuthenticator;

    @Before("execution(public * com.mmadu.service.providers.AuthenticationService.authenticate(..)) && "
            + "args(authRequest)")
    public void authenticateRequest(AuthenticateRequest authRequest){
        String domain = authRequest.getDomain();
        String tokenValue = request.getHeader(DOMAIN_AUTH_TOKEN_FIELD);
        preAuthorize(domain, tokenValue);
    }

    private void preAuthorize(String domain, String tokenValue) {
        apiAuthenticator.authenticateDomain(domain, tokenValue);
    }

}
