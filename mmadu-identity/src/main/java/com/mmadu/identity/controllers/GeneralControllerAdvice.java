package com.mmadu.identity.controllers;

import com.mmadu.identity.exceptions.AuthorizationException;
import com.mmadu.identity.exceptions.CredentialNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GeneralControllerAdvice {

    @ExceptionHandler(CredentialNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleCredentialNotFound() {

    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleAuthenticationException(AuthorizationException ex) {
        return Map.of("error", "invalid_client_details",
                "description", ex.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGenericErrors(AuthorizationException ex) {
        return Map.of("error", "unexpected_error",
                "description", ex.getMessage()
        );
    }
}
