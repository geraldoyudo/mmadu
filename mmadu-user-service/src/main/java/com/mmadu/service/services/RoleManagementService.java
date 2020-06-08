package com.mmadu.service.services;

import com.mmadu.service.models.AddRoleAuthorityRequest;
import com.mmadu.service.models.RoleData;
import com.mmadu.service.models.SaveRoleRequest;

import java.util.List;

public interface RoleManagementService {

    void saveRoles(String domainId, List<SaveRoleRequest> roleRequests);

    List<RoleData> getRoles(String domainId);

    void deleteRole(String domainId, String identifier);

    void grantUserRoles(String domainId, String userId, List<String> roleIdentifiers);

    void revokeUserRoles(String domainId, String userId, List<String> roleIdentifiers);

    void addAuthorityToRole(String domainId, List<AddRoleAuthorityRequest> roleAuthorities);

    void removeAuthorityFromRole(String domainId, List<AddRoleAuthorityRequest> roleAuthorities);
}
