package com.mmadu.identity.controllers;

import com.mmadu.identity.exceptions.AuthorizationException;
import com.mmadu.identity.exceptions.CredentialNotFoundException;
import com.mmadu.identity.exceptions.DomainNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GeneralControllerAdvice {

    @ExceptionHandler(CredentialNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleCredentialNotFound() {

    }

    @ExceptionHandler(DomainNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleDomainNotFound() {

    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleAuthenticationException(AuthorizationException ex) {
        return Map.of("error", "invalid_client_details",
                "description", ex.getMessage()
        );
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            BindException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidRequest() {
        return Map.of("error", "invalid_request",
                "description", "invalid request"
        );
    }

    @ExceptionHandler(
            RepositoryConstraintViolationException.class
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleRepositoryConstraintVioationError(RepositoryConstraintViolationException ex) {
        return Map.of("code", "23",
                "message",
                String.format("Validation failed: %s",
                        ex.getErrors().getAllErrors()
                                .stream()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                .collect(Collectors.joining(","))));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGenericErrors(Exception ex) {
        log.error("unexpected error", ex);
        return Map.of("error", "unexpected_error",
                "description", ex.getMessage()
        );
    }
}
