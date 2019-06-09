package com.mmadu.service.models;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class AuthenticateRequest {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
