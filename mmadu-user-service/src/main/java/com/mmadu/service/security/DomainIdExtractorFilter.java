package com.mmadu.service.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.service.utilities.DomainAuthenticationConstants;
import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.ContentCachingRequestWrapper;

public class DomainIdExtractorFilter extends GenericFilterBean {
    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest currentRequest = (HttpServletRequest) servletRequest;
        String method = currentRequest.getMethod();
        if(method.equals(HttpMethod.POST.name()) || method.equals(HttpMethod.PUT.name()) ){
            BufferedReader reader = currentRequest.getReader();
            JsonNode root = objectMapper.readTree(reader);
            JsonNode domainNode = root.at("/domain");
            String domainId = "";
            if(domainNode != null){
                domainId = domainNode.asText();
            }
            if(StringUtils.isEmpty(domainId)){
               domainNode = root.at("/domainId");
               if(domainNode != null){
                   domainId = domainNode.asText();
               }
            }
            currentRequest.setAttribute("domainId", domainId);
        }
        chain.doFilter(currentRequest, servletResponse);
    }
}