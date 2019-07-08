package com.mmadu.service.controllers;

import com.geraldoyudo.kweeri.core.converters.QueryNotSupportedException;
import com.geraldoyudo.kweeri.core.mapping.QueryProcessingException;
import com.mmadu.service.exceptions.*;
import com.mmadu.service.models.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
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

    @ExceptionHandler({DomainAuthenticationException.class, InvalidDomainCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizedException() {
        return new ErrorResponse("225", "Unauthorized");
    }

    @ExceptionHandler({
            QueryNotSupportedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleQueryNotSupported(QueryNotSupportedException ex) {
        return new ErrorResponse("230", ex.getMessage());
    }

    @ExceptionHandler({
            QueryProcessingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleQueryProcessingException(QueryProcessingException ex) {
        return new ErrorResponse("235", ex.getMessage());
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

    @ExceptionHandler({
            Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneralError(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return new ErrorResponse("300", "Unexpected error");
    }
}
