package com.mmadu.registration.controllers;

import com.mmadu.registration.exceptions.PropertyValueExpectationFailedException;
import com.mmadu.registration.models.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GeneralControllerAdvice {

    @ExceptionHandler(PropertyValueExpectationFailedException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ErrorResponse handlePropertyValueExpectationFailed() {
        return new ErrorResponse("10", "property not found in user");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpectedErrors(Exception ex) {
        log.error("An unexpected error has occurred", ex);
        return new ErrorResponse("99", "unexpected error");
    }
}
