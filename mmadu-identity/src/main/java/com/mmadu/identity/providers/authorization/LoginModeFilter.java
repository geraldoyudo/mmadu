package com.mmadu.identity.providers.authorization;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Component
public class LoginModeFilter implements Filter {


    public static final String LOGIN_MODE = "loginMode";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Optional<String> loginMode = Optional.ofNullable(servletRequest.getParameter(LOGIN_MODE));
        loginMode.ifPresent(mode -> this.setLoginMode(mode, servletRequest, servletResponse));
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void setLoginMode(String loginMode, ServletRequest request, ServletResponse servletResponse) {
        request.setAttribute(LOGIN_MODE, loginMode);
        if (request instanceof HttpServletRequest) {
            ((HttpServletRequest) request).getSession().setAttribute(LOGIN_MODE, loginMode);
        }
    }
}
