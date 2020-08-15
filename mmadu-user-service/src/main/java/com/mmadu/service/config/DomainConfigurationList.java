package com.mmadu.service.config;

import com.mmadu.service.entities.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;
import java.util.*;

@Component
@ConfigurationProperties(prefix = "mmadu.domain-config")
public class DomainConfigurationList {
    private List<DomainItem> domains = new ArrayList<>();

    public List<DomainItem> getDomains() {
        return domains;
    }

    public void setDomains(List<DomainItem> domains) {
        this.domains = domains;
    }

    public static class DomainItem {
        @NotEmpty
        private String name;
        private String id;
        private String jwkSetUri;
        private List<UserItem> users = Collections.emptyList();
        private List<AuthorityItem> authorities = Collections.emptyList();
        private List<GroupItem> groups = Collections.emptyList();
        private List<RoleItem> roles = Collections.emptyList();
        private List<RoleAuthorityItem> roleAuthorities = Collections.emptyList();
        private List<UserAuthorityItem> userAuthorities = Collections.emptyList();
        private List<UserGroupItem> userGroups = Collections.emptyList();
        private List<UserRoleItem> userRoles = Collections.emptyList();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getJwkSetUri() {
            return jwkSetUri;
        }

        public void setJwkSetUri(String jwkSetUri) {
            this.jwkSetUri = jwkSetUri;
        }

        public List<UserItem> getUsers() {
            return users;
        }

        public void setUsers(List<UserItem> users) {
            this.users = users;
        }

        public List<AuthorityItem> getAuthorities() {
            return authorities;
        }

        public void setAuthorities(List<AuthorityItem> authorities) {
            this.authorities = authorities;
        }

        public List<GroupItem> getGroups() {
            return groups;
        }

        public void setGroups(List<GroupItem> groups) {
            this.groups = groups;
        }

        public List<RoleItem> getRoles() {
            return roles;
        }

        public void setRoles(List<RoleItem> roles) {
            this.roles = roles;
        }

        public List<RoleAuthorityItem> getRoleAuthorities() {
            return roleAuthorities;
        }

        public void setRoleAuthorities(List<RoleAuthorityItem> roleAuthorities) {
            this.roleAuthorities = roleAuthorities;
        }

        public List<UserAuthorityItem> getUserAuthorities() {
            return userAuthorities;
        }

        public void setUserAuthorities(List<UserAuthorityItem> userAuthorities) {
            this.userAuthorities = userAuthorities;
        }

        public List<UserGroupItem> getUserGroups() {
            return userGroups;
        }

        public void setUserGroups(List<UserGroupItem> userGroups) {
            this.userGroups = userGroups;
        }

        public List<UserRoleItem> getUserRoles() {
            return userRoles;
        }

        public void setUserRoles(List<UserRoleItem> userRoles) {
            this.userRoles = userRoles;
        }

        public AppDomain toEntity() {
            AppDomain domain = new AppDomain();
            domain.setName(name);
            domain.setId(id);
            domain.setJwkSetUri(jwkSetUri);
            return domain;
        }
    }

    public static class UserItem {
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
        @NotEmpty
        private String externalId;
        private Map<String, Object> properties = Collections.emptyMap();

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getExternalId() {
            return externalId;
        }

        public void setExternalId(String externalId) {
            this.externalId = externalId;
        }

        public Map<String, Object> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

        public AppUser toEntity(String domainId) {
            AppUser user = new AppUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setDomainId(domainId);
            if (!StringUtils.isEmpty(externalId)) {
                user.setExternalId(externalId);
            }
            user.setProperties(properties);
            return user;
        }
    }

    public static class AuthorityItem {
        @NotEmpty
        private String identifier;
        @NotEmpty
        private String name;
        @NotEmpty
        private String description;

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Authority toEntity(String domainId) {
            Authority auth = new Authority();
            auth.setDomainId(domainId);
            auth.setDescription(description);
            auth.setName(name);
            auth.setIdentifier(identifier);
            return auth;
        }
    }

    public static class GroupItem {
        @NotEmpty
        private String identifier;
        @NotEmpty
        private String name;
        @NotEmpty
        private String description;
        private String parent;

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public Group toEntity(String domainId, GroupResolver resolver) {
            Group group = new Group();
            group.setDomainId(domainId);
            group.setIdentifier(identifier);
            group.setName(name);
            group.setDescription(description);
            if (!StringUtils.isEmpty(parent)) {
                group.setParent(
                        resolver.getGroup(parent)
                                .orElseThrow(() -> new IllegalArgumentException("Could not resolve group parent "
                                        + parent + "for group " + group.getIdentifier()))
                );
            }
            return group;
        }
    }

    @FunctionalInterface
    public interface GroupResolver {
        Optional<Group> getGroup(String identifier);
    }

    public static class RoleItem {
        @NotEmpty
        private String identifier;
        @NotEmpty
        private String name;
        @NotEmpty
        private String description;

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Role toEntity(String domainId) {
            Role role = new Role();
            role.setName(name);
            role.setIdentifier(identifier);
            role.setDescription(description);
            role.setName(name);
            role.setDomainId(domainId);
            return role;
        }
    }

    public static class RoleAuthorityItem {
        @NotEmpty
        private String role;
        @NotEmpty
        private String authority;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getAuthority() {
            return authority;
        }

        public void setAuthority(String authority) {
            this.authority = authority;
        }

        public RoleAuthority toEntity(String domainId, AuthorityResolver authorityResolver,
                                      RoleResolver roleResolver) {
            RoleAuthority roleAuthority = new RoleAuthority();
            roleAuthority.setAuthority(
                    authorityResolver.getAuthority(authority).orElseThrow(() ->
                            new IllegalArgumentException("authority not found: " + authority))
            );
            roleAuthority.setRole(
                    roleResolver.getRole(role).orElseThrow(() ->
                            new IllegalArgumentException("role not found: " + role))
            );
            roleAuthority.setDomainId(domainId);
            return roleAuthority;
        }
    }

    @FunctionalInterface
    public interface RoleResolver {
        Optional<Role> getRole(String identifier);
    }

    @FunctionalInterface
    public interface AuthorityResolver {
        Optional<Authority> getAuthority(String identifier);
    }

    public static class UserAuthorityItem {
        @NotEmpty
        private String user;
        @NotEmpty
        private String authority;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getAuthority() {
            return authority;
        }

        public void setAuthority(String authority) {
            this.authority = authority;
        }

        public UserAuthority toEntity(String domainId, AuthorityResolver authorityResolver,
                                      UserResolver userResolver) {
            UserAuthority userAuthority = new UserAuthority();
            userAuthority.setAuthority(
                    authorityResolver.getAuthority(authority).orElseThrow(() ->
                            new IllegalArgumentException("authority not found: " + authority))
            );
            userAuthority.setUser(
                    userResolver.getUser(user).orElseThrow(() ->
                            new IllegalArgumentException("user not found: " + user))
            );
            userAuthority.setDomainId(domainId);
            return userAuthority;
        }
    }

    @FunctionalInterface
    public interface UserResolver {
        Optional<AppUser> getUser(String externalIdOrUsername);
    }

    public static class UserGroupItem {
        @NotEmpty
        private String user;
        @NotEmpty
        private String group;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public UserGroup toEntity(String domainId, GroupResolver groupResolver,
                                  UserResolver userResolver) {
            UserGroup userGroup = new UserGroup();
            userGroup.setGroup(
                    groupResolver.getGroup(group).orElseThrow(() ->
                            new IllegalArgumentException("group not found: " + group))
            );
            userGroup.setUser(
                    userResolver.getUser(user).orElseThrow(() ->
                            new IllegalArgumentException("user not found: " + user))
            );
            userGroup.setDomainId(domainId);
            return userGroup;
        }
    }

    public static class UserRoleItem {
        @NotEmpty
        private String user;
        @NotEmpty
        private String role;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public UserRole toEntity(String domainId, RoleResolver roleResolver,
                                 UserResolver userResolver) {
            UserRole userRole = new UserRole();
            userRole.setRole(
                    roleResolver.getRole(role).orElseThrow(() ->
                            new IllegalArgumentException("role not found: " + role))
            );
            userRole.setUser(
                    userResolver.getUser(user).orElseThrow(() ->
                            new IllegalArgumentException("user not found: " + user))
            );
            userRole.setDomainId(domainId);
            return userRole;
        }
    }
}
