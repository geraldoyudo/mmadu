package com.mmadu.service.controllers;

import com.mmadu.service.models.AuthenticateRequest;
import com.mmadu.service.models.AuthenticateResponse;
import com.mmadu.service.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping(path = "/domains/{domainId}/authenticate")
    @PreAuthorize("hasAuthority('user.authenticate')")
    public AuthenticateResponse authenticateUser(@PathVariable("domainId") String domainId,
                                                 @RequestBody AuthenticateRequest request) {
        return authenticationService.authenticate(domainId, request);
    }
}
