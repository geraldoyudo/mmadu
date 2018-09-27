package com.mmadu.service.interceptors;

import com.mmadu.service.exceptions.InvalidDomainCredentialsException;
import com.mmadu.service.models.AuthenticateRequest;
import com.mmadu.service.service.AuthenticateApiAuthenticator;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AuthenticationServiceAdvice {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private AuthenticateApiAuthenticator apiAuthenticator;

    @Before("execution(public * com.mmadu.service.service.AuthenticationService.authenticate(..)) && "
            + "args(authRequest)")
    public void authenticateRequest(AuthenticateRequest authRequest){
        String domain = authRequest.getDomain();
        String tokenValue = request.getHeader("domain-auth-token");
        preAuthorize(domain, tokenValue);
    }

    private void preAuthorize(String domain, String tokenValue) {
        apiAuthenticator.authenticateDomain(domain, tokenValue);
    }

}
