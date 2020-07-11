package com.mmadu.service.services;

import com.mmadu.service.entities.*;
import com.mmadu.service.exceptions.NotFoundException;
import com.mmadu.service.exceptions.UserNotFoundException;
import com.mmadu.service.models.PagedList;
import com.mmadu.service.models.RoleAuthorityUpdateRequest;
import com.mmadu.service.models.RoleData;
import com.mmadu.service.models.SaveRoleRequest;
import com.mmadu.service.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RoleManagementServiceImpl implements RoleManagementService {
    private RoleRepository roleRepository;
    private UserRoleRepository userRoleRepository;
    private RoleAuthorityRepository roleAuthorityRepository;
    private AuthorityRepository authorityRepository;
    private AppUserRepository appUserRepository;

    @Autowired
    public void setRoleAuthorityRepository(RoleAuthorityRepository roleAuthorityRepository) {
        this.roleAuthorityRepository = roleAuthorityRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Autowired
    public void setAuthorityRepository(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Autowired
    public void setAppUserRepository(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    @Transactional
    public void saveRoles(String domainId, List<SaveRoleRequest> roleRequests) {
        roleRequests.forEach(role -> this.createOrUpdateRole(domainId, role));
    }

    private void createOrUpdateRole(String domainId, SaveRoleRequest role) {
        roleRepository.findByDomainIdAndIdentifier(domainId, role.getIdentifier())
                .ifPresentOrElse(oldRole -> updateRole(oldRole, role),
                        () -> createRole(domainId, role));
    }

    private void updateRole(Role oldRole, SaveRoleRequest role) {
        oldRole.setDescription(role.getDescription());
        oldRole.setIdentifier(role.getIdentifier());
        oldRole.setName(role.getName());
        Role savedRole = roleRepository.save(oldRole);
        updateAuthorities(savedRole, role.getAuthorities());
    }

    private void updateAuthorities(Role role, List<String> authorities) {
        if (authorities.isEmpty()) {
            return;
        }
        roleAuthorityRepository.deleteByDomainIdAndRoleId(role.getDomainId(), role.getId());
        doUpdateAuthorities(role, authorities);
    }

    private void doUpdateAuthorities(Role role, List<String> authorities) {
        List<RoleAuthority> roleAuthorities = authorityRepository.findByDomainIdAndIdentifierIn(role.getDomainId(), authorities)
                .stream()
                .map(auth -> new RoleAuthority(role.getDomainId(), role, auth))
                .collect(Collectors.toList());
        roleAuthorityRepository.saveAll(roleAuthorities);
    }


    private void createRole(String domainId, SaveRoleRequest role) {
        Role newRole = new Role();
        newRole.setName(role.getName());
        newRole.setIdentifier(role.getIdentifier());
        newRole.setDescription(role.getDescription());
        newRole.setDomainId(domainId);
        newRole = roleRepository.save(newRole);
        doUpdateAuthorities(newRole, role.getAuthorities());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoleData> getRoles(String domainId, Pageable p) {
        Page<RoleData> roleDataPage = roleRepository.findByDomainId(domainId, p)
                .map(Role::roleData);
        return new PagedList<>(roleDataPage.getContent(), roleDataPage.getPageable(), roleDataPage.getTotalElements());
    }

    @Override
    @Transactional
    public void deleteRole(String domainId, String identifier) {
        Role role = roleRepository.findByDomainIdAndIdentifier(domainId, identifier).orElseThrow(() -> {
            throw new NotFoundException("role not found");
        });
        roleAuthorityRepository.deleteByDomainIdAndRoleId(domainId, role.getId());
        userRoleRepository.deleteByDomainIdAndRoleId(domainId, role.getId());
        roleRepository.deleteById(role.getId());
    }

    @Override
    @Transactional
    public void grantUserRoles(String domainId, String userId, List<String> roleIdentifiers) {
        AppUser user = appUserRepository.findByDomainIdAndExternalId(domainId, userId)
                .orElseThrow(UserNotFoundException::new);
        List<Role> roles = roleRepository.findByDomainIdAndIdentifierIn(domainId, roleIdentifiers);
        if (roles.size() != roleIdentifiers.size()) {
            throw new NotFoundException("some or all roles could not be found");
        }
        roles.forEach(role -> addUserRoleIfNotExist(domainId, user, role));
    }

    private void addUserRoleIfNotExist(String domainId, AppUser user, Role role) {
        if (!userRoleRepository.existsByDomainIdAndUserIdAndRoleId(domainId, user.getId(), role.getId())) {
            UserRole userRole = new UserRole();
            userRole.setDomainId(domainId);
            userRole.setUser(user);
            userRole.setRole(role);
            userRoleRepository.save(userRole);
        }
    }

    @Override
    @Transactional
    public void revokeUserRoles(String domainId, String userId, List<String> roleIdentifiers) {
        AppUser user = appUserRepository.findByDomainIdAndExternalId(domainId, userId)
                .orElseThrow(UserNotFoundException::new);
        List<Role> roles = roleRepository.findByDomainIdAndIdentifierIn(domainId, roleIdentifiers);
        if (roles.size() != roleIdentifiers.size()) {
            throw new NotFoundException("some or all authorities could not be found");
        }
        roles.forEach(auth -> removeUserRolesIfExists(domainId, user, auth));
    }

    private void removeUserRolesIfExists(String domainId, AppUser user, Role role) {
        if (userRoleRepository.existsByDomainIdAndUserIdAndRoleId(domainId, user.getId(), role.getId())) {
            userRoleRepository.deleteByDomainIdAndUserIdAndRoleId(domainId, user.getId(), role.getId());
        }
    }

    @Override
    @Transactional
    public void addAuthorityToRole(String domainId, List<RoleAuthorityUpdateRequest> roleAuthorities) {
        extractRoleAuthorityAndPerform(domainId, roleAuthorities, this::addIfNotExists);
    }

    private void extractRoleAuthorityAndPerform(String domainId, List<RoleAuthorityUpdateRequest> roleAuthorities,
                                                RoleAuthorityCallable roleAuthorityCallable) {
        List<String> roleIdentifiers = roleAuthorities.stream()
                .map(RoleAuthorityUpdateRequest::getRole)
                .distinct()
                .collect(Collectors.toList());
        List<String> authorityIdentifiers = roleAuthorities.stream()
                .flatMap(ri -> ri.getAuthorities().stream())
                .distinct()
                .collect(Collectors.toList());
        List<Role> roles = roleRepository.findByDomainIdAndIdentifierIn(domainId, roleIdentifiers);
        if (roles.size() != roleIdentifiers.size()) {
            throw new NotFoundException("role not found");
        }
        List<Authority> authorities = authorityRepository.findByDomainIdAndIdentifierIn(domainId, authorityIdentifiers);
        if (authorities.size() != authorityIdentifiers.size()) {
            throw new NotFoundException("authorities not found");
        }
        Map<String, Role> roleMap = roles.stream()
                .collect(Collectors.toMap(Role::getIdentifier, r -> r));
        Map<String, Authority> authorityMap = authorities.stream()
                .collect(Collectors.toMap(Authority::getIdentifier, a -> a));

        roleAuthorities
                .stream()
                .flatMap(roleAuth -> toRoleAuth(domainId, roleAuth, roleMap, authorityMap))
                .forEach(roleAuthorityCallable::call);
    }

    private Stream<RoleAuthority> toRoleAuth(String domainId, RoleAuthorityUpdateRequest request, Map<String, Role> roleMap,
                                             Map<String, Authority> authorityMap) {
        return request.getAuthorities()
                .stream()
                .map(auth -> {
                    RoleAuthority roleAuthority = new RoleAuthority();
                    roleAuthority.setDomainId(domainId);
                    roleAuthority.setRole(roleMap.get(request.getRole()));
                    roleAuthority.setAuthority(authorityMap.get(auth));
                    return roleAuthority;
                });
    }

    private void addIfNotExists(RoleAuthority roleAuthority) {
        if (!roleAuthorityRepository.existsByDomainIdAndRoleIdAndAuthorityId(roleAuthority.getDomainId(),
                roleAuthority.getRole().getId(), roleAuthority.getAuthority().getId())) {
            roleAuthorityRepository.save(roleAuthority);
        }
    }

    @Override
    @Transactional
    public void removeAuthorityFromRole(String domainId, List<RoleAuthorityUpdateRequest> roleAuthorities) {
        extractRoleAuthorityAndPerform(domainId, roleAuthorities, this::removeIfExists);
    }

    private void removeIfExists(RoleAuthority roleAuthority) {
        roleAuthorityRepository.findByDomainIdAndRoleIdAndAuthorityId(roleAuthority.getDomainId(),
                roleAuthority.getRole().getId(), roleAuthority.getAuthority().getId())
                .ifPresent(roleAuthorityRepository::delete);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> getUserRoles(String domainId, String userId) {
        AppUser user = appUserRepository.findByDomainIdAndExternalId(domainId, userId)
                .orElseThrow(UserNotFoundException::new);
        return userRoleRepository.findByDomainIdAndUserId(domainId, user.getId())
                .stream()
                .map(ur -> ur.getRole().getIdentifier())
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> getAuthoritiesForRoles(String domainId, List<String> roleIdentifiers) {
        List<Role> roles = roleRepository.findByDomainIdAndIdentifierIn(domainId, roleIdentifiers);
        if (roles.size() != roleIdentifiers.size()) {
            throw new NotFoundException("role not found");
        }
        return roleAuthorityRepository.findByDomainIdAndRoleIn(domainId, roles)
                .stream()
                .map(ra -> ra.getAuthority().getIdentifier())
                .collect(Collectors.toSet());
    }

    interface RoleAuthorityCallable {
        void call(RoleAuthority roleAuthority);
    }
}
