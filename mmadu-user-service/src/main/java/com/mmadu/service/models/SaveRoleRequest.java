package com.mmadu.service.models;

import lombok.Data;

import java.util.List;

@Data
public class SaveRoleRequest {
    private String identifier;
    private String name;
    private String description;
    private List<String> authorities;
}
