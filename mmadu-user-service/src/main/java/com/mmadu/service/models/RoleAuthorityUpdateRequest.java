package com.mmadu.service.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class RoleAuthorityUpdateRequest {
    @NotEmpty(message = "role is required")
    private String role;
    @NotEmpty(message = "authority is required")
    private List<String> authorities;
}
