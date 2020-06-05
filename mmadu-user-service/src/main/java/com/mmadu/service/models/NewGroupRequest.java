package com.mmadu.service.models;

import javax.validation.constraints.NotEmpty;

public class NewGroupRequest {
    @NotEmpty(message = "identifier is required")
    private String identifier;
    @NotEmpty(message = "name is required")
    private String name;
    @NotEmpty(message = "description is required")
    private String description;
    @NotEmpty(message = "parentGroup is required")
    private String parentGroup;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(String parentGroup) {
        this.parentGroup = parentGroup;
    }
}
