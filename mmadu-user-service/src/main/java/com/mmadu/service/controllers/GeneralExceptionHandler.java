package com.mmadu.service.controllers;

import com.mmadu.service.exceptions.DomainAuthenticationException;
import com.mmadu.service.exceptions.DomainNotFoundException;
import com.mmadu.service.exceptions.InvalidDomainCredentialsException;
import com.mmadu.service.exceptions.TokenNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler({
            DomainNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFoundException(){

    }

    @ExceptionHandler({DomainAuthenticationException.class, InvalidDomainCredentialsException.class,
            TokenNotFoundException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleUnauthorizedException(){

    }
}
