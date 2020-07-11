package com.mmadu.identity.controllers.token;

import com.mmadu.identity.exceptions.AuthenticationException;
import com.mmadu.identity.exceptions.AuthorizationException;
import com.mmadu.identity.exceptions.TokenNotFoundException;
import com.mmadu.identity.models.token.TokenIntrospectionRequest;
import com.mmadu.identity.models.token.TokenIntrospectionResponse;
import com.mmadu.identity.models.token.error.InvalidRequest;
import com.mmadu.identity.models.token.error.TokenError;
import com.mmadu.identity.models.token.error.UnauthorizedClient;
import com.mmadu.identity.security.clients.EnsureClientAuthentication;
import com.mmadu.identity.services.token.TokenIntrospectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

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

    @ExceptionHandler(TokenNotFoundException.class)
    public Map<String, Boolean> returnNotActive() {
        return Map.of("active", false);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public TokenError handleValidationErrors() {
        return new InvalidRequest("parameters.invalid", "");
    }

    @ExceptionHandler({
            AuthorizationException.class,
            AuthenticationException.class
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public TokenError handleAuthorizationErrors() {
        return new UnauthorizedClient("unauthorized", "");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public TokenError handleGeneralExceptions(Exception ex) {
        log.error("Unexpected error", ex);
        return new InvalidRequest(ex.getMessage(), "");
    }
}
