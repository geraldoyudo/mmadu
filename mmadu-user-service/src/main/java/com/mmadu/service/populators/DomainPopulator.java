package com.mmadu.service.populators;

import com.mmadu.service.config.DomainConfigurationList;
import com.mmadu.service.entities.*;
import com.mmadu.service.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class DomainPopulator {
    private AppDomainRepository appDomainRepository;
    private DomainConfigurationList domainConfigurationList;
    private AppUserRepository appUserRepository;
    private AuthorityRepository authorityRepository;
    private GroupRepository groupRepository;
    private RoleRepository roleRepository;
    private UserGroupRepository userGroupRepository;
    private UserRoleRepository userRoleRepository;
    private UserAuthorityRepository userAuthorityRepository;
    private RoleAuthorityRepository roleAuthorityRepository;

    @Autowired
    public void setAppDomainRepository(AppDomainRepository appDomainRepository) {
        this.appDomainRepository = appDomainRepository;
    }

    @Autowired
    public void setDomainConfigurationList(DomainConfigurationList domainConfigurationList) {
        this.domainConfigurationList = domainConfigurationList;
    }

    @Autowired
    public void setAppUserRepository(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Autowired
    public void setAuthorityRepository(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setRoleAuthorityRepository(RoleAuthorityRepository roleAuthorityRepository) {
        this.roleAuthorityRepository = roleAuthorityRepository;
    }

    @Autowired
    public void setUserAuthorityRepository(UserAuthorityRepository userAuthorityRepository) {
        this.userAuthorityRepository = userAuthorityRepository;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Autowired
    public void setUserGroupRepository(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void setUpDomains() {
        List<DomainConfigurationList.DomainItem> unInitializedDomains = Optional.ofNullable(domainConfigurationList.getDomains())
                .orElse(Collections.emptyList())
                .stream()
                .filter(domainItem -> !appDomainRepository.existsByName(domainItem.getName()))
                .collect(Collectors.toList());
        if (!unInitializedDomains.isEmpty()) {
            initializeDomains(unInitializedDomains);
        }
    }

    public void initializeDomains(List<DomainConfigurationList.DomainItem> domainItems) {
        domainItems.forEach(
                this::initializeDomain
        );
    }

    private void initializeDomain(DomainConfigurationList.DomainItem item) {
        AppDomain domain = appDomainRepository.save(item.toEntity());
        DomainContext context = new DomainContext(domain);
        initializeDomainUsers(item.getUsers(), context);
        initializeDomainAuthorities(item.getAuthorities(), context);
        initializeDomainRoles(item.getRoles(), context);
        initializeDomainRoleAuthorities(item.getRoleAuthorities(), context);
        initializeDomainUserAuthorities(item.getUserAuthorities(), context);
        initializeDomainUserRoles(item.getUserRoles(), context);
        initializeDomainGroups(item.getGroups(), context);
        initializeDomainUserGroups(item.getUserGroups(), context);
    }

    private void initializeDomainUsers(List<DomainConfigurationList.UserItem> users, DomainContext context) {
        List<AppUser> appUsers = users.stream()
                .map(u -> u.toEntity(context.getDomainId()))
                .collect(Collectors.toList());
        context.addUsers(appUserRepository.saveAll(appUsers));
    }

    private void initializeDomainAuthorities(List<DomainConfigurationList.AuthorityItem> authorities, DomainContext context) {
        List<Authority> appAuthorities = authorities.stream()
                .map(a -> a.toEntity(context.getDomainId()))
                .collect(Collectors.toList());
        context.addAuthorities(authorityRepository.saveAll(appAuthorities));
    }

    private void initializeDomainRoles(List<DomainConfigurationList.RoleItem> roles, DomainContext context) {
        List<Role> appRoles = roles.stream()
                .map(r -> r.toEntity(context.getDomainId()))
                .collect(Collectors.toList());
        context.addRoles(roleRepository.saveAll(appRoles));
    }

    private void initializeDomainRoleAuthorities(List<DomainConfigurationList.RoleAuthorityItem> roleAuthorities,
                                                 DomainContext context) {
        List<RoleAuthority> appRoleAuthorities = roleAuthorities.stream()
                .map(ra -> ra.toEntity(context.getDomainId(), context, context))
                .collect(Collectors.toList());
        roleAuthorityRepository.saveAll(appRoleAuthorities);
    }

    private void initializeDomainUserAuthorities(List<DomainConfigurationList.UserAuthorityItem> userAuthorities, DomainContext context) {
        List<UserAuthority> appUserAuthorities = userAuthorities.stream()
                .map(ua -> ua.toEntity(context.getDomainId(), context, context))
                .collect(Collectors.toList());
        userAuthorityRepository.saveAll(appUserAuthorities);
    }

    private void initializeDomainUserRoles(List<DomainConfigurationList.UserRoleItem> userRoles, DomainContext context) {
        List<UserRole> appUserRoles = userRoles.stream()
                .map(ur -> ur.toEntity(context.getDomainId(), context, context))
                .collect(Collectors.toList());
        userRoleRepository.saveAll(appUserRoles);
    }

    private void initializeDomainGroups(List<DomainConfigurationList.GroupItem> groups, DomainContext context) {
        for (DomainConfigurationList.GroupItem group : groups) {
            Group appGroup = group.toEntity(context.getDomainId(), context);
            Group parent = appGroup.getParent();
            appGroup.setParent(null);
            Group savedGroup = groupRepository.save(appGroup);
            Optional.ofNullable(parent).ifPresent(p -> {
                savedGroup.setParent(p);
                groupRepository.saveAll(List.of(p, savedGroup));
            });
            context.addGroups(List.of(savedGroup));
        }
    }

    private void initializeDomainUserGroups(List<DomainConfigurationList.UserGroupItem> userGroups, DomainContext context) {
        List<UserGroup> appUserGroups = userGroups.stream()
                .map(ug -> ug.toEntity(context.getDomainId(), context, context))
                .collect(Collectors.toList());
        userGroupRepository.saveAll(appUserGroups);
    }

    private static class DomainContext implements DomainConfigurationList.GroupResolver,
            DomainConfigurationList.RoleResolver, DomainConfigurationList.AuthorityResolver,
            DomainConfigurationList.UserResolver {
        private final AppDomain domain;
        private final Map<String, AppUser> externalIdUserMap = new HashMap<>();
        private final Map<String, AppUser> usernameUserMap = new HashMap<>();
        private final Map<String, Role> identifierRoleMap = new HashMap<>();
        private final Map<String, Authority> identifierAuthorityMap = new HashMap<>();
        private final Map<String, Group> identifierGroupMap = Collections.emptyMap();

        public DomainContext(AppDomain domain) {
            this.domain = domain;
        }

        public void addUsers(List<AppUser> users) {
            users.forEach(user -> {
                externalIdUserMap.put(user.getExternalId(), user);
                usernameUserMap.put(user.getUsername(), user);
            });
        }

        public void addRoles(List<Role> roles) {
            roles.forEach(role -> identifierRoleMap.put(role.getIdentifier(), role));
        }

        public void addAuthorities(List<Authority> authorities) {
            authorities.forEach(authority -> identifierAuthorityMap.put(authority.getIdentifier(), authority));
        }

        public void addGroups(List<Group> groups) {
            groups.forEach(group -> identifierGroupMap.put(group.getIdentifier(), group));
        }

        public String getDomainId() {
            return domain.getId();
        }

        @Override
        public Optional<AppUser> getUser(String externalIdOrUsername) {
            if (externalIdUserMap.containsKey(externalIdOrUsername)) {
                return Optional.of(externalIdUserMap.get(externalIdOrUsername));
            }
            return Optional.ofNullable(usernameUserMap.get(externalIdOrUsername));
        }

        @Override
        public Optional<Authority> getAuthority(String identifier) {
            return Optional.ofNullable(identifierAuthorityMap.get(identifier));
        }

        @Override
        public Optional<Role> getRole(String identifier) {
            return Optional.ofNullable(identifierRoleMap.get(identifier));
        }

        @Override
        public Optional<Group> getGroup(String identifier) {
            return Optional.ofNullable(identifierGroupMap.get(identifier));
        }
    }
}
