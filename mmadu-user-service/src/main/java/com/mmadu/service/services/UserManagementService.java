package com.mmadu.service.services;

import com.mmadu.service.model.UserView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserManagementService {

    void createUser(String domainId, UserView userView);

    Page<UserView> getAllUsers(String domainId, Pageable pageable);
}
