package com.mmadu.registration.controllers;

import com.mmadu.registration.models.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GeneralControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleUnexpectedErrors(Exception ex) {
        log.error("An unexpected error has occurred", ex);
        return new ErrorResponse("99", "unexpected error");
    }
}
