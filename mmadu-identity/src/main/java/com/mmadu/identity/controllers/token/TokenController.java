package com.mmadu.identity.controllers.token;

import com.mmadu.identity.models.token.TokenRequest;
import com.mmadu.identity.models.token.TokenResponse;
import com.mmadu.identity.models.token.error.TokenError;
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
@RequestMapping("/oauth/token")
@Slf4j
public class TokenController {
    private TokenRequestValidator tokenRequestValidator;
    private ValidationErrorProcessor validationErrorProcessor;

    @Autowired
    public void setTokenRequestValidator(TokenRequestValidator tokenRequestValidator) {
        this.tokenRequestValidator = tokenRequestValidator;
    }

    @Autowired
    public void setValidationErrorProcessor(ValidationErrorProcessor validationErrorProcessor) {
        this.validationErrorProcessor = validationErrorProcessor;
    }

    @InitBinder
    public void registerValidators(WebDataBinder binder) {
        binder.addValidators(tokenRequestValidator);
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TokenResponse getToken(@Valid TokenRequest request) {
        log.info("Processing token request {}", request);
        return new TokenResponse();
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public TokenError handleValidationErrors(BindException ex) {
        return validationErrorProcessor.processError(ex);
    }
}
