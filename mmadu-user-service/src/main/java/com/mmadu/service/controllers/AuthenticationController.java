package com.mmadu.service.controllers;

import com.mmadu.service.models.AuthenticateRequest;
import com.mmadu.service.models.AuthenticateResponse;
import com.mmadu.service.providers.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping(path = "/domains/{domainId}/authenticate")
    public AuthenticateResponse authenticateUser(@PathVariable("domainId") String domainId,
                                                 @RequestBody AuthenticateRequest request) {
        return authenticationService.authenticate(domainId, request);
    }
}
