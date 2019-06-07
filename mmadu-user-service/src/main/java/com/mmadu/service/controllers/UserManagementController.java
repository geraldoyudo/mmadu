package com.mmadu.service.controllers;

import com.mmadu.service.model.UserView;
import com.mmadu.service.services.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/domains/{domainId}/users")
public class UserManagementController {
    @Autowired
    private UserManagementService userManagementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserView user,
                           @PathVariable("domainId") String  domainId) {
        userManagementService.createUser(domainId, user);
    }

    @GetMapping
    public Page<UserView> getAllUsers(@PathVariable("domainId") String domainId, Pageable p){
        return userManagementService.getAllUsers(domainId, p);
    }
}
