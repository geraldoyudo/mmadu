package com.mmadu.service.security;

import com.mmadu.service.entities.AppUser;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class RepositorySecurity {

    @HandleBeforeCreate
    @PreAuthorize("hasAuthority('user.create')")
    public void createUser(@P("user") AppUser user) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('user.update')")
    public void updateUser(@P("user") AppUser user) {

    }

}
