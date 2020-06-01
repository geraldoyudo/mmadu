package com.mmadu.identity.controllers.token;

import com.mmadu.identity.models.token.TokenRequest;
import com.mmadu.identity.models.token.TokenResponse;
import com.mmadu.identity.validators.token.TokenRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/oauth/token")
public class TokenController {
    private TokenRequestValidator tokenRequestValidator;

    @Autowired
    public void setTokenRequestValidator(TokenRequestValidator tokenRequestValidator) {
        this.tokenRequestValidator = tokenRequestValidator;
    }

    @InitBinder
    public void registerValidators(WebDataBinder binder){
        binder.addValidators(tokenRequestValidator);
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public TokenResponse getToken(@RequestBody @Valid TokenRequest request){

        return new TokenResponse();
    }
}
