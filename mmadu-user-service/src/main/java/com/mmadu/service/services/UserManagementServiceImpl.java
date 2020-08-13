package com.mmadu.service.services;

import com.mmadu.service.entities.AppUser;
import com.mmadu.service.exceptions.DomainNotFoundException;
import com.mmadu.service.exceptions.DuplicationException;
import com.mmadu.service.exceptions.UserNotFoundException;
import com.mmadu.service.models.*;
import com.mmadu.service.providers.UniqueUserIdGenerator;
import com.mmadu.service.repositories.AppDomainRepository;
import com.mmadu.service.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class UserManagementServiceImpl implements UserManagementService {
    private AppUserRepository appUserRepository;
    private AppDomainRepository appDomainRepository;
    private UniqueUserIdGenerator uniqueUserIdGenerator;
    private GroupService groupService;
    private RoleManagementService roleManagementService;
    private AuthorityManagementService authorityManagementService;

    @Autowired
    public void setRoleManagementService(RoleManagementService roleManagementService) {
        this.roleManagementService = roleManagementService;
    }

    @Autowired
    public void setAuthorityManagementService(AuthorityManagementService authorityManagementService) {
        this.authorityManagementService = authorityManagementService;
    }

    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    @Autowired
    public void setAppUserRepository(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Autowired
    public void setAppDomainRepository(AppDomainRepository appDomainRepository) {
        this.appDomainRepository = appDomainRepository;
    }

    @Autowired
    public void setUniqueUserIdGenerator(UniqueUserIdGenerator uniqueUserIdGenerator) {
        this.uniqueUserIdGenerator = uniqueUserIdGenerator;
    }

    @Override
    @Transactional
    public void createUser(String domainId, UserView userView) {
        if (userView == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
        if (StringUtils.isEmpty(userView.getUsername())) {
            throw new IllegalArgumentException("user missing username");
        }
        if (userView.getPassword() == null) {
            throw new IllegalArgumentException("user missing password");
        }
        if (!appDomainRepository.existsById(domainId)) {
            throw new DomainNotFoundException();
        }
        if (appUserRepository.existsByUsernameAndDomainId(userView.getUsername(), domainId) ||
                appUserRepository.existsByExternalIdAndDomainId(userView.getId(), domainId)) {
            throw new DuplicationException("user already exists");
        }
        if (StringUtils.isEmpty(userView.getId())) {
            userView.setId(uniqueUserIdGenerator.generateUniqueId(domainId));
        }
        AppUser appUser = new AppUser(domainId, userView);
        appUser = appUserRepository.save(appUser);
        addUserRolesIfExists(userView, appUser);
        addUserToGroupsIfExists(userView, appUser);
        addUserAuthoritiesIfExists(userView, appUser);
    }

    private void addUserRolesIfExists(UserView userView, AppUser appUser) {
        List<String> roles = Optional.ofNullable(userView.getRoles()).orElse(emptyList());
        if (!roles.isEmpty()) {
            roleManagementService.grantUserRoles(appUser.getDomainId(), appUser.getExternalId(), roles);
        }
    }

    private void addUserToGroupsIfExists(UserView userView, AppUser appUser) {
        List<String> groups = Optional.ofNullable(userView.getGroups()).orElse(emptyList());
        if (!groups.isEmpty()) {
            groups.stream()
                    .map(group -> new NewGroupUserRequest(appUser.getExternalId(), group))
                    .forEach(req -> groupService.addUserToGroup(appUser.getDomainId(), req));
        }
    }

    private void addUserAuthoritiesIfExists(UserView userView, AppUser appUser) {
        List<String> authorities = Optional.ofNullable(userView.getAuthorities()).orElse(emptyList());
        if (!authorities.isEmpty()) {
            authorityManagementService.grantUserAuthorities(appUser.getDomainId(), appUser.getExternalId(), authorities);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserView> getAllUsers(String domainId, Pageable pageable) {
        if (!appDomainRepository.existsById(domainId)) {
            throw new DomainNotFoundException();
        }
        Page<UserView> userViewPage = appUserRepository.findByDomainId(domainId, pageable)
                .map(AppUser::userView);
        return new PagedList<>(userViewPage.getContent(), userViewPage.getPageable(), userViewPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public UserView getUserByDomainIdAndExternalId(String domainId, String externalId) {
        if (!appDomainRepository.existsById(domainId)) {
            throw new DomainNotFoundException();
        }
        return appUserRepository.findByDomainIdAndExternalId(domainId, externalId)
                .orElseThrow(UserNotFoundException::new).userView();
    }

    @Override
    @Transactional
    public void deleteUserByDomainAndExternalId(String domainId, String externalId) {
        if (!appDomainRepository.existsById(domainId)) {
            throw new DomainNotFoundException();
        }
        if (!appUserRepository.existsByExternalIdAndDomainId(externalId, domainId)) {
            throw new UserNotFoundException();
        }
        appUserRepository.deleteByDomainIdAndExternalId(domainId, externalId);
    }

    @Override
    @Transactional
    public void updateUser(String domainId, String externalId, UserView userView) {
        if (userView == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
        if (!appDomainRepository.existsById(domainId)) {
            throw new DomainNotFoundException();
        }
        AppUser appUser = appUserRepository.findByDomainIdAndExternalId(domainId, externalId)
                .orElseThrow(UserNotFoundException::new);
        appUser.setExternalId(userView.getId());
        appUser.setUsername(userView.getUsername());
        appUser.setPassword(userView.getPassword());
        appUser.setProperties(userView.getProperties());
        appUserRepository.save(appUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserView getUserByDomainIdAndUsername(String domainId, String username) {
        if (!appDomainRepository.existsById(domainId)) {
            throw new DomainNotFoundException();
        }
        return appUserRepository.findByUsernameAndDomainId(username, domainId)
                .orElseThrow(UserNotFoundException::new).userView();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserView> queryUsers(String domainId, String query, Pageable pageable) {
        String resultantQuery = ensureQueryAndDomainParameters(domainId, query);
        Page<UserView> userViewPage = appUserRepository.queryForUsers(resultantQuery, pageable)
                .map(AppUser::userView);
        return new PagedList<>(userViewPage.getContent(), userViewPage.getPageable(), userViewPage.getTotalElements());
    }

    private String ensureQueryAndDomainParameters(String domainId, String query) {
        if (!appDomainRepository.existsById(domainId)) {
            throw new DomainNotFoundException();
        }
        String domainClause = String.format("(domainId equals '%s')", domainId);
        String resultantQuery = query;
        if (StringUtils.isEmpty(resultantQuery)) {
            resultantQuery = domainClause;
        } else {
            resultantQuery = resultantQuery.replaceAll(" id ", " externalId ")
                    .replaceAll("\\(id", "(externalId").replaceAll("^id", "externalId")
                    + " and " + domainClause;
        }
        return resultantQuery;
    }

    @Override
    @Transactional
    public void patchUpdateUsers(String domainId, String query, UpdateRequest updateRequest) {
        if (updateRequest == null) {
            throw new IllegalArgumentException("Update request cannot be null");
        }
        if (updateRequest.getUpdates() == null || updateRequest.getUpdates().isEmpty()) {
            throw new IllegalArgumentException("User patches cannot be empty");
        }
        String resultantQuery = ensureQueryAndDomainParameters(domainId, query);
        appUserRepository.updateUsers(resultantQuery, updateRequest);
    }

    @Override
    public void resetUserPassword(String domainId, String userId, String newPassword) {
        AppUser user = appUserRepository.findByDomainIdAndExternalId(domainId, userId)
                .orElseThrow(UserNotFoundException::new);
        user.setPassword(newPassword);
        appUserRepository.save(user);
    }

    @Override
    public void setPropertyValidationState(String domainId, String userId, PropertyValidationStateUpdateRequest request) {
        AppUser user = appUserRepository.findByDomainIdAndExternalId(domainId, userId)
                .orElseThrow(UserNotFoundException::new);
        user.addPropertyValidationStateEntry(request.getPropertyName(), request.isValid());
        appUserRepository.save(user);
    }

    @Override
    public void setUserEnabled(String domainId, String userId, SetEnabledRequest request) {
        AppUser user = appUserRepository.findByDomainIdAndExternalId(domainId, userId)
                .orElseThrow(UserNotFoundException::new);
        user.setEnabled(request.isEnabled());
        appUserRepository.save(user);
    }

    @Override
    public void setUserLocked(String domainId, String userId, SetLockedRequest request) {
        AppUser user = appUserRepository.findByDomainIdAndExternalId(domainId, userId)
                .orElseThrow(UserNotFoundException::new);
        user.setLocked(request.isLocked());
        appUserRepository.save(user);
    }

    @Override
    public void setUserActive(String domainId, String userId, SetActiveRequest request) {
        AppUser user = appUserRepository.findByDomainIdAndExternalId(domainId, userId)
                .orElseThrow(UserNotFoundException::new);
        user.setActive(request.isActive());
        appUserRepository.save(user);
    }

    @Override
    public void setCredentialsExpired(String domainId, String userId, SetCredentialsExpiredRequest request) {
        AppUser user = appUserRepository.findByDomainIdAndExternalId(domainId, userId)
                .orElseThrow(UserNotFoundException::new);
        user.setCredentialExpired(request.isCredentialExpired());
        appUserRepository.save(user);
    }
}
