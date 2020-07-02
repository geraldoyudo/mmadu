package com.mmadu.identity.controllers.token;

import com.mmadu.identity.exceptions.TokenException;
import com.mmadu.identity.models.token.TokenRequest;
import com.mmadu.identity.models.token.TokenResponse;
import com.mmadu.identity.models.token.error.InvalidRequest;
import com.mmadu.identity.models.token.error.TokenError;
import com.mmadu.identity.services.token.TokenService;
import com.mmadu.identity.validators.token.TokenRequestValidator;
import com.mmadu.identity.validators.token.ValidationErrorProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/clients/token")
@Slf4j
public class TokenController {
    private TokenRequestValidator tokenRequestValidator;
    private ValidationErrorProcessor validationErrorProcessor;
    private TokenService tokenService;

    @Autowired
    public void setTokenRequestValidator(TokenRequestValidator tokenRequestValidator) {
        this.tokenRequestValidator = tokenRequestValidator;
    }

    @Autowired
    public void setValidationErrorProcessor(ValidationErrorProcessor validationErrorProcessor) {
        this.validationErrorProcessor = validationErrorProcessor;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @InitBinder
    public void registerValidators(WebDataBinder binder) {
        binder.addValidators(tokenRequestValidator);
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TokenResponse getToken(@Valid TokenRequest request) {
        log.debug("Processing token request {}", request);
        return tokenService.getToken(request);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public TokenError handleValidationErrors(BindException ex) {
        return validationErrorProcessor.processError(ex);
    }

    @ExceptionHandler(TokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public TokenError handleTokenException(TokenException ex) {
        return ex.getTokenError();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public TokenError handleGeneralExceptions(Exception ex) {
        log.error("Unexpected error", ex);
        return new InvalidRequest(ex.getMessage(), "");
    }
}
