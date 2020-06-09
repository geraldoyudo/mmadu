package com.mmadu.service.services;

import com.mmadu.service.config.DatabaseConfig;
import com.mmadu.service.entities.*;
import com.mmadu.service.models.RoleAuthorityUpdateRequest;
import com.mmadu.service.models.RoleData;
import com.mmadu.service.models.SaveRoleRequest;
import com.mmadu.service.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Import({
        DatabaseConfig.class,
        RoleManagementServiceImpl.class
})
class RoleManagementServiceImplTest {
    static final String DOMAIN_ID = "test-domain";
    public static final String EXTERNAL_ID = "82982";

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleAuthorityRepository roleAuthorityRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private RoleManagementService roleManagementService;

    @AfterEach
    void clear() {
        roleRepository.deleteAll();
        roleAuthorityRepository.deleteAll();
        userRoleRepository.deleteAll();
        appUserRepository.deleteAll();
        authorityRepository.deleteAll();
    }

    @Test
    void saveRoles() {
        Authority authority = createAndSaveAuthority();
        SaveRoleRequest role = new SaveRoleRequest();
        role.setName("test");
        role.setIdentifier("test-role");
        role.setDescription("test-description");
        role.setAuthorities(List.of(authority.getIdentifier()));

        roleManagementService.saveRoles(DOMAIN_ID, List.of(role));

        assertAll(
                () -> assertEquals(1, roleRepository.count()),
                () -> assertEquals(1, roleAuthorityRepository.count()),
                () -> assertEquals(role.getIdentifier(), roleAuthorityRepository.findAll().get(0).getRole().getIdentifier())
        );
    }

    private Authority createAndSaveAuthority() {
        Authority authority = new Authority();
        authority.setDomainId(DOMAIN_ID);
        authority.setDescription("test-auth-description");
        authority.setIdentifier("test-auth");
        authority.setName("test-auth-name");
        return authorityRepository.save(authority);
    }

    @Test
    void getRoles() {
        Role role = createAndSaveRole();
        Page<RoleData> roles = roleManagementService.getRoles(DOMAIN_ID, Pageable.unpaged());

        assertAll(
                () -> assertEquals(1, roles.getTotalElements()),
                () -> assertEquals(role.getIdentifier(), roles.getContent().get(0).getIdentifier())
        );

    }

    private Role createAndSaveRole() {
        Role role = new Role();
        role.setDomainId(DOMAIN_ID);
        role.setDescription("role-description");
        role.setIdentifier("test-role");
        role.setName("test-role-name");
        return roleRepository.save(role);
    }

    @Test
    void deleteRole() {
        Role role = createAndSaveRole();
        Authority authority = createAndSaveAuthority();
        AppUser user = createAndSaveUser();
        RoleAuthority roleAuthority = createRoleAuthorityLink(role, authority);
        UserRole userRole = createUserRoleLink(user, role);
        roleManagementService.deleteRole(DOMAIN_ID, role.getIdentifier());

        assertAll(
                () -> assertFalse(roleRepository.existsById(role.getIdentifier())),
                () -> assertFalse(userRoleRepository.existsById(userRole.getId())),
                () -> assertFalse(roleAuthorityRepository.existsById(roleAuthority.getId()))
        );
    }

    private AppUser createAndSaveUser() {
        AppUser user = new AppUser();
        user.setUsername("username");
        user.setPassword("pass");
        user.setExternalId("28329383");
        user.setDomainId(DOMAIN_ID);
        return appUserRepository.save(user);
    }

    private RoleAuthority createRoleAuthorityLink(Role role, Authority authority) {
        RoleAuthority ra = new RoleAuthority();
        ra.setAuthority(authority);
        ra.setRole(role);
        ra.setDomainId(DOMAIN_ID);
        return roleAuthorityRepository.save(ra);
    }

    private UserRole createUserRoleLink(AppUser user, Role role) {
        UserRole ur = new UserRole();
        ur.setRole(role);
        ur.setUser(user);
        ur.setDomainId(DOMAIN_ID);
        return userRoleRepository.save(ur);
    }

    @Test
    void grantUserRoles() {
        Role role = createAndSaveRole();
        AppUser user = createAndSaveUser();

        roleManagementService.grantUserRoles(DOMAIN_ID, user.getExternalId(), List.of(role.getIdentifier()));

        assertAll(
                () -> assertEquals(1, userRoleRepository.count()),
                () -> assertEquals(role.getId(), userRoleRepository.findAll().get(0).getRole().getId())
        );
    }

    @Test
    void revokeUserRoles() {
        Role role = createAndSaveRole();
        AppUser user = createAndSaveUser();
        UserRole userRole = createUserRoleLink(user, role);

        roleManagementService.revokeUserRoles(DOMAIN_ID, user.getExternalId(), List.of(role.getIdentifier()));

        assertAll(
                () -> assertEquals(0, userRoleRepository.count()),
                () -> assertTrue(userRoleRepository.findById(userRole.getId()).isEmpty())
        );
    }

    @Test
    void addAuthorityToRole() {
        Role role = createAndSaveRole();
        Authority authority = createAndSaveAuthority();

        roleManagementService.addAuthorityToRole(DOMAIN_ID, List.of(updateRequest(role.getIdentifier(), authority.getIdentifier())));

        assertAll(
                () -> assertTrue(roleAuthorityRepository.existsByDomainIdAndRoleIdAndAuthorityId(DOMAIN_ID, role.getId(), authority.getId()))
        );
    }

    private RoleAuthorityUpdateRequest updateRequest(String role, String authority) {
        RoleAuthorityUpdateRequest request = new RoleAuthorityUpdateRequest();
        request.setAuthorities(List.of(authority));
        request.setRole(role);
        return request;
    }

    @Test
    void removeAuthorityFromRole() {
        Role role = createAndSaveRole();
        Authority authority = createAndSaveAuthority();
        createRoleAuthorityLink(role, authority);

        roleManagementService.removeAuthorityFromRole(DOMAIN_ID, List.of(updateRequest(role.getIdentifier(), authority.getIdentifier())));

        assertAll(
                () -> assertFalse(roleAuthorityRepository.existsByDomainIdAndRoleIdAndAuthorityId(DOMAIN_ID, role.getId(), authority.getId()))
        );
    }

    @Test
    void getUserRoles() {
        Role role = createAndSaveRole();
        AppUser user = createAndSaveUser();
        createUserRoleLink(user, role);

        assertEquals(
                Set.of(role.getIdentifier()),
                roleManagementService.getUserRoles(DOMAIN_ID, user.getExternalId())
        );
    }

    @Test
    void getAuthoritiesForRoles() {
        Role role = createAndSaveRole();
        Authority authority = createAndSaveAuthority();
        createRoleAuthorityLink(role, authority);

        assertEquals(
                Set.of(authority.getIdentifier()),
                roleManagementService.getAuthoritiesForRoles(DOMAIN_ID, List.of(role.getIdentifier()))
        );
    }
}