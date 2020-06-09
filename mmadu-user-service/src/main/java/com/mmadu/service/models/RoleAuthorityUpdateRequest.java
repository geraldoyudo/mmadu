package com.mmadu.service.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class RoleAuthorityUpdateRequest {
    @NotEmpty(message = "role is required")
    private String role;
    @NotNull(message = "authorities is required")
    @Size(min = 1, message = "authorities is required")
    private List<String> authorities;
}
