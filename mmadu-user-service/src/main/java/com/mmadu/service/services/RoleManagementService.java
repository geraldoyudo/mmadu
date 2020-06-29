package com.mmadu.service.services;

import com.mmadu.service.models.RoleAuthorityUpdateRequest;
import com.mmadu.service.models.RoleData;
import com.mmadu.service.models.SaveRoleRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface RoleManagementService {

    void saveRoles(String domainId, List<SaveRoleRequest> roleRequests);

    Page<RoleData> getRoles(String domainId, Pageable p);

    void deleteRole(String domainId, String identifier);

    void grantUserRoles(String domainId, String userId, List<String> roleIdentifiers);

    void revokeUserRoles(String domainId, String userId, List<String> roleIdentifiers);

    void addAuthorityToRole(String domainId, List<RoleAuthorityUpdateRequest> roleAuthorities);

    void removeAuthorityFromRole(String domainId, List<RoleAuthorityUpdateRequest> roleAuthorities);

    Set<String> getUserRoles(String domainId, String userId);

    Set<String> getAuthoritiesForRoles(String domainId, List<String> roleIdentifiers);
}
