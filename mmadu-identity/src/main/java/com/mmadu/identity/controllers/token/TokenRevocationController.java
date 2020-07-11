package com.mmadu.identity.controllers.token;

import com.mmadu.identity.exceptions.AuthenticationException;
import com.mmadu.identity.exceptions.AuthorizationException;
import com.mmadu.identity.exceptions.TokenNotFoundException;
import com.mmadu.identity.models.token.TokenRevocationRequest;
import com.mmadu.identity.models.token.error.InvalidRequest;
import com.mmadu.identity.models.token.error.TokenError;
import com.mmadu.identity.models.token.error.UnauthorizedClient;
import com.mmadu.identity.models.token.error.UnsupportedTokenType;
import com.mmadu.identity.security.clients.EnsureClientAuthentication;
import com.mmadu.identity.services.token.TokenRevocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/clients/revokeToken")
@Slf4j
public class TokenRevocationController {
    private TokenRevocationService tokenRevocationService;

    @Autowired
    public void setTokenRevocationService(TokenRevocationService tokenRevocationService) {
        this.tokenRevocationService = tokenRevocationService;
    }

    @EnsureClientAuthentication
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void revokeToken(@Valid TokenRevocationRequest request) {
        log.debug("Processing token revocation request {}", request);
        tokenRevocationService.revokeToken(request);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public TokenError returnRevokeSuccessful() {
        return new UnsupportedTokenType("token.not.found", "");
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
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public TokenError handleGeneralExceptions(Exception ex) {
        log.error("Unexpected error", ex);
        return new InvalidRequest(ex.getMessage(), "");
    }
}
