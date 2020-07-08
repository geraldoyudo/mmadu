package com.mmadu.identity.controllers.token;

import com.mmadu.identity.models.token.TokenIntrospectionRequest;
import com.mmadu.identity.models.token.TokenIntrospectionResponse;
import com.mmadu.identity.security.clients.EnsureClientAuthentication;
import com.mmadu.identity.services.token.TokenIntrospectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/clients/checkToken")
@Slf4j
public class TokenIntrospectionController {
    private TokenIntrospectionService tokenIntrospectionService;

    @Autowired
    public void setTokenIntrospectionService(TokenIntrospectionService tokenIntrospectionService) {
        this.tokenIntrospectionService = tokenIntrospectionService;
    }

    @EnsureClientAuthentication
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TokenIntrospectionResponse checkToken(@Valid TokenIntrospectionRequest request) {
        log.debug("Processing token introspection request {}", request);
        return tokenIntrospectionService.getTokenDetails(request);
    }
}
