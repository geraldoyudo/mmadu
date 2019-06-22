package com.mmadu.service.services;

import com.mmadu.service.models.AuthenticateRequest;
import com.mmadu.service.models.AuthenticateResponse;

public interface AuthenticationService {

    AuthenticateResponse authenticate(String domainId, AuthenticateRequest authRequest);
}
