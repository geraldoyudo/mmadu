package com.mmadu.notifications.service.controllers;

import com.mmadu.notifications.service.exceptions.ProfileNotFoundException;
import com.mmadu.notifications.service.exceptions.ProviderNotFoundException;
import com.mmadu.notifications.service.models.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GeneralControllerAdvice {

    @ExceptionHandler(ProfileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleProfileNotFound() {
        return ErrorResponse.builder()
                .code("5")
                .message("Profile not found")
                .build();
    }

    @ExceptionHandler(ProviderNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponse handleProviderNotFound() {
        return ErrorResponse.builder()
                .code("10")
                .message("No provider can handle your request")
                .build();
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            BindException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgument(Exception ex) {
        return ErrorResponse.builder()
                .code("15")
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ErrorResponse handleIllegalStateException(IllegalStateException ex) {
        return ErrorResponse.builder()
                .code("17")
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return ErrorResponse.builder()
                .code("22")
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return ErrorResponse.builder()
                .code("24")
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGeneralErrors(Exception ex) {
        log.error("An unexpected error occurred", ex);
        return ErrorResponse.builder()
                .code("99")
                .message("An unexpected error occurred")
                .build();
    }
}
