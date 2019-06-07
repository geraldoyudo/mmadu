package com.mmadu.service.controllers;

import com.mmadu.service.exceptions.*;
import com.mmadu.service.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler({
            DomainNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDomainNotFoundException() {
        return new ErrorResponse("200", "domain not found");
    }

    @ExceptionHandler({
            UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException() {
        return new ErrorResponse("210", "user not found");
    }

    @ExceptionHandler({
            IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ErrorResponse("215", ex.getMessage());
    }

    @ExceptionHandler({
            DuplicationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDuplicationException(DuplicationException ex) {
        return new ErrorResponse("220", ex.getMessage());
    }

    @ExceptionHandler({DomainAuthenticationException.class, InvalidDomainCredentialsException.class,
            TokenNotFoundException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizedException() {
        return new ErrorResponse("225", "Unauthorized");
    }
}
