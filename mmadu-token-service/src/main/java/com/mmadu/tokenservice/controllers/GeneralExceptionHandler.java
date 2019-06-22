package com.mmadu.tokenservice.controllers;

import com.mmadu.tokenservice.exceptions.TokenNotFoundException;
import com.mmadu.tokenservice.models.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GeneralExceptionHandler.class);

    @ExceptionHandler({
            IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ErrorResponse("215", ex.getMessage());
    }

    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return new ErrorResponse("240", ex.getMessage());
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return new ErrorResponse("245", ex.getMessage());
    }

    @ExceptionHandler(TokenNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleTokenNotFoundException() {
        return new ErrorResponse("250", "token not found");
    }

    @ExceptionHandler({
            Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneralError(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return new ErrorResponse("300", "Unexpected error");
    }

}
