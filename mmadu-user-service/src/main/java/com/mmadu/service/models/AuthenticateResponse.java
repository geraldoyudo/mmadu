package com.mmadu.service.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticateResponse {

    private AuthenticationStatus status = AuthenticationStatus.AUTHENTICATED;
}
