package com.mmadu.identity.providers.authorization;

import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.services.client.MmaduClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class ClientDomainPopulatorFilter implements Filter {
    private MmaduClientService mmaduClientService;

    @Autowired
    public void setMmaduClientService(MmaduClientService mmaduClientService) {
        this.mmaduClientService = mmaduClientService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Optional<String> clientIdentifier = Optional.ofNullable(servletRequest.getParameter("client_id"));
        clientIdentifier.ifPresent(id -> this.setDomain(id, servletRequest, servletResponse));
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void setDomain(String clientIdentifier, ServletRequest request, ServletResponse servletResponse) {
        mmaduClientService.loadClientByIdentifier(clientIdentifier)
                .ifPresent(client -> setDomainInformation(request, servletResponse, client));
    }

    private void setDomainInformation(ServletRequest request, ServletResponse servletResponse, MmaduClient client) {
        request.setAttribute("domain", client.getDomainId());
        if (servletResponse instanceof HttpServletResponse) {
            Cookie domainCookie = new Cookie("domain", client.getDomainId());
            domainCookie.setSecure(false);
            domainCookie.setMaxAge(-1);
            domainCookie.setPath("/");
            ((HttpServletResponse) servletResponse).addCookie(domainCookie);
        }
    }
}
