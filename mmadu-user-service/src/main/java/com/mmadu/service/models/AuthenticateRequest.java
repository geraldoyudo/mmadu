package com.mmadu.service.models;

import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticateRequest {
    @NotEmpty
    private String domain;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
