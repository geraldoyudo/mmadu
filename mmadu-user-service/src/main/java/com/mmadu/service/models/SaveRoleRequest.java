package com.mmadu.service.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class SaveRoleRequest {
    @NotEmpty(message = "identifier is required")
    private String identifier;
    @NotEmpty(message = "name is required")
    private String name;
    @NotEmpty(message = "description is required")
    private String description;
    @NotNull(message = "authorities is required")
    @Size(min = 1, message = "authorities is required")
    private List<String> authorities;
}
