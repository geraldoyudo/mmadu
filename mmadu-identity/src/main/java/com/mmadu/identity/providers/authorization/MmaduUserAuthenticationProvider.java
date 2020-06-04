package com.mmadu.identity.providers.authorization;

import com.mmadu.identity.models.user.MmaduUser;
import com.mmadu.identity.services.user.MmaduUserService;
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
    private HttpServletRequest httpRequest;
    private MmaduUserService mmaduUserService;

    @Autowired
    public void setHttpRequest(HttpServletRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Autowired
    public void setMmaduUserService(MmaduUserService mmaduUserService) {
        this.mmaduUserService = mmaduUserService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authentication instanceof MmaduUserAuthenticationToken){
            return authentication;
        }
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        String domain = (String) Optional.ofNullable(httpRequest.getAttribute("domain")).orElse("0");

        mmaduUserService.authenticate(domain, username, password);
        MmaduUser user = mmaduUserService.loadUserByUsernameAndDomainId(username, domain)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
        return new MmaduUserAuthenticationToken(user, "", domain);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.equals(aClass) ||
                MmaduUserAuthenticationToken.class.equals(aClass);
    }
}
