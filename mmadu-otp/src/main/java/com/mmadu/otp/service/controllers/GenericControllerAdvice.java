package com.mmadu.otp.service.controllers;

import com.mmadu.otp.service.models.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GenericControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(GenericControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGeneralExceptions(Exception ex) {
        log.error("An unexpected error occurred", ex);
        return ErrorResponse.error("99", "An unexpected error occurred");
    }
}
