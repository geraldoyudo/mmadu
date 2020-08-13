package com.mmadu.service.services;

import com.mmadu.service.models.*;
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

    void resetUserPassword(String domainId, String userId, String newPassword);

    void changeUserPassword(String domainId, String userId, ChangeUserPasswordRequest request);

    void setPropertyValidationState(String domainId, String userId, PropertyValidationStateUpdateRequest request);

    void setUserEnabled(String domainId, String userId, SetEnabledRequest request);

    void setUserLocked(String domainId, String userId, SetLockedRequest request);

    void setUserActive(String domainId, String userId, SetActiveRequest request);

    void setCredentialsExpired(String domainId, String userId, SetCredentialsExpiredRequest request);
}
