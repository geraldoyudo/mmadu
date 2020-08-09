package com.mmadu.notifications.service.controllers;

import com.mmadu.notifications.service.exceptions.ProviderNotFoundException;
import com.mmadu.notifications.service.models.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GeneralControllerAdvice {

    @ExceptionHandler(ProviderNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponse handleProviderNotFound() {
        return ErrorResponse.builder()
                .code("10")
                .message("No provider can handle your requst")
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGeneralErrors() {
        return ErrorResponse.builder()
                .code("99")
                .message("An unexpected error occurred")
                .build();
    }
}
