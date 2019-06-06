package com.mmadu.service.services;

import com.mmadu.service.model.UserView;

public interface UserManagementService {

    void createUser(String domainId, UserView userView);
}
