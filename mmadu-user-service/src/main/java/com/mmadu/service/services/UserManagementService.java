package com.mmadu.service.services;

import com.mmadu.service.models.UpdateRequest;
import com.mmadu.service.models.UserView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserManagementService {

    void createUser(String domainId, UserView userView);

    Page<UserView> getAllUsers(String domainId, Pageable pageable);

    UserView getUserByDomainIdAndExternalId(String domainId, String externalId);

    void deleteUserByDomainAndExternalId(String domainId, String externalId);

    void updateUser(String domainId, String externalId, UserView userView);

    UserView getUserByDomainIdAndUsername(String domainId, String username);

    Page<UserView> queryUsers(String domainId, String query, Pageable pageable);

    void patchUpdateUsers(String domainId, String query, UpdateRequest updateRequest);
}
