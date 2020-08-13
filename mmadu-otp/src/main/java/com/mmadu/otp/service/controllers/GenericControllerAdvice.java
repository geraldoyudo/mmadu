package com.mmadu.otp.service.controllers;

import com.mmadu.otp.service.exceptions.DomainNotFoundException;
import com.mmadu.otp.service.exceptions.OtpNotFoundException;
import com.mmadu.otp.service.exceptions.ProfileNotFoundException;
import com.mmadu.otp.service.exceptions.ProviderNotFoundException;
import com.mmadu.otp.service.models.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GenericControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(GenericControllerAdvice.class);

    @ExceptionHandler({
            DomainNotFoundException.class,
            OtpNotFoundException.class,
            ProfileNotFoundException.class,
            ProviderNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(Exception ex) {
        return ErrorResponse.error("10", ex.getMessage());
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            BindException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgument(Exception ex) {
        return ErrorResponse.error("15", ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ErrorResponse handleIllegalStateException(IllegalStateException ex) {
        return ErrorResponse.error("17", ex.getMessage());
    }

    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return ErrorResponse.error("22", ex.getMessage());
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return ErrorResponse.error("24", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGeneralExceptions(Exception ex) {
        log.error("An unexpected error occurred", ex);
        return ErrorResponse.error("99", "An unexpected error occurred");
    }
}
