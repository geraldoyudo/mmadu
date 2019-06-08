package com.mmadu.service.providers;

import com.mmadu.service.models.AuthenticateRequest;
import com.mmadu.service.models.AuthenticateResponse;

public interface AuthenticationService {

    AuthenticateResponse authenticate(AuthenticateRequest authRequest);
}
