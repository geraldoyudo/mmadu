package com.mmadu.identity.providers.authorization;

import com.mmadu.identity.models.user.MmaduUser;
import com.mmadu.identity.services.user.MmaduUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;


@Component
@Qualifier("mmaduUser")
public class MmaduUserAuthenticationProvider implements AuthenticationProvider {
    private String defaultDomain;
    private HttpSession httpSession;
    private MmaduUserService mmaduUserService;

    @Autowired
    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @Autowired
    public void setMmaduUserService(MmaduUserService mmaduUserService) {
        this.mmaduUserService = mmaduUserService;
    }

    @Value("${mmadu.identity.default-domain:0}")
    public void setDefaultDomain(String defaultDomain) {
        this.defaultDomain = defaultDomain;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authentication instanceof MmaduUserAuthenticationToken){
            return authentication;
        }
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        String domain = (String) Optional.ofNullable(httpSession.getAttribute("domain")).orElse(defaultDomain);

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
