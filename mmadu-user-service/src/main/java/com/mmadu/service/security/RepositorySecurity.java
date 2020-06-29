package com.mmadu.service.security;

import com.mmadu.service.entities.*;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class RepositorySecurity {

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('domain.update')")
    public void updateDomain(@P("domain") AppDomain domain) {

    }

    @HandleBeforeCreate
    @PreAuthorize("hasAuthority('user.create')")
    public void createUser(@P("user") AppUser user) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('user.update')")
    public void updateUser(@P("user") AppUser user) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasAuthority('user.delete')")
    public void deleteUser(@P("user") AppUser user) {

    }

    @HandleBeforeCreate
    @PreAuthorize("hasAuthority('authority.create')")
    public void createAuthority(@P("authority") Authority authority) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('authority.update')")
    public void updateAuthority(@P("authority") Authority authority) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasAuthority('authority.delete')")
    public void deleteAuthority(@P("user") Authority authority) {

    }

    @HandleBeforeCreate
    @PreAuthorize("hasAuthority('group.create')")
    public void createGroup(@P("group") Group group) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('group.update')")
    public void updateGroup(@P("group") Group group) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasAuthority('group.delete')")
    public void deleteGroup(@P("group") Group group) {

    }

    @HandleBeforeCreate
    @PreAuthorize("hasAuthority('role.create')")
    public void createRole(@P("role") Role role) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('role.update')")
    public void updateRole(@P("role") Role role) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasAuthority('role.delete')")
    public void deleteRole(@P("role") Role role) {

    }

    @HandleBeforeCreate
    @PreAuthorize("hasAuthority('role_authority.create')")
    public void createRoleAuthority(@P("roleAuthority") RoleAuthority roleAuthority) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('role_authority.update')")
    public void updateRoleAuthority(@P("roleAuthority") RoleAuthority roleAuthority) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasAuthority('role_authority.delete')")
    public void deleteRoleAuthority(@P("roleAuthority") RoleAuthority roleAuthority) {

    }

    @HandleBeforeCreate
    @PreAuthorize("hasAuthority('user_authority.create')")
    public void createUserAuthority(@P("userAuthority") UserAuthority userAuthority) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasAuthority('user_authority.update')")
    public void updateUserAuthority(@P("userAuthority") UserAuthority userAuthority) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasAuthority('user_authority.delete')")
    public void deleteUserAuthority(@P("userAuthority") UserAuthority userAuthority) {

    }

    @HandleBeforeCreate
    @PreAuthorize("hasGroup('user_group.create')")
    public void createUserGroup(@P("userGroup") UserGroup userGroup) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasGroup('user_group.update')")
    public void updateUserGroup(@P("userGroup") UserGroup userGroup) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasAuthority('user_group.delete')")
    public void deleteUserGroup(@P("userGroup") UserGroup userGroup) {

    }

    @HandleBeforeCreate
    @PreAuthorize("hasRole('user_role.create')")
    public void createUserRole(@P("userRole") UserRole userRole) {

    }

    @HandleBeforeSave
    @PreAuthorize("hasRole('user_role.update')")
    public void updateUserRole(@P("userRole") UserRole userRole) {

    }

    @HandleBeforeDelete
    @PreAuthorize("hasRole('user_role.delete')")
    public void deleteUserRole(@P("userRole") UserRole userRole) {

    }
}
