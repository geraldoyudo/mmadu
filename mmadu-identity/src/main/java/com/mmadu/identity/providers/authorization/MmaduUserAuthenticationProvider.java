package com.mmadu.identity.providers.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@Component
@Qualifier("mmaduUser")
public class MmaduUserAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private HttpServletRequest httpRequest;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        String domain = (String) Optional.ofNullable(httpRequest.getAttribute("domain")).orElse("0");
        if(username.equals("gerald")){
            return new MmaduUserAuthenticationToken(username, password, domain);
        } else {
            throw new UsernameNotFoundException("username not found");
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
