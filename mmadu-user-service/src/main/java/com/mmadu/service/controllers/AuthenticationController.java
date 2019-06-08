package com.mmadu.service.controllers;

import com.mmadu.service.models.AuthenticateRequest;
import com.mmadu.service.models.AuthenticateResponse;
import com.mmadu.service.providers.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping(path = "/authenticate")
    public AuthenticateResponse authenticateUser(@RequestBody AuthenticateRequest request) {
        return authenticationService.authenticate(request);
    }
}
