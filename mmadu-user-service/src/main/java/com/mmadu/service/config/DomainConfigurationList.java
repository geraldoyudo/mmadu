package com.mmadu.service.config;

import com.mmadu.service.entities.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;
import java.util.*;

@Component
@ConfigurationProperties(prefix = "mmadu.domain-config")
@Data
public class DomainConfigurationList {
    private List<DomainItem> domains = new ArrayList<>();

    @Data
    public static class DomainItem {
        @NotEmpty
        private String name;
        private String id;
        private List<UserItem> users = Collections.emptyList();
        private List<AuthorityItem> authorities = Collections.emptyList();
        private List<GroupItem> groups = Collections.emptyList();
        private List<RoleItem> roles = Collections.emptyList();
        private List<RoleAuthorityItem> roleAuthorities = Collections.emptyList();
        private List<UserAuthorityItem> userAuthorities = Collections.emptyList();
        private List<UserGroupItem> userGroups = Collections.emptyList();
        private List<UserRoleItem> userRoles = Collections.emptyList();

        public AppDomain toEntity() {
            AppDomain domain = new AppDomain();
            domain.setName(name);
            domain.setId(id);
            return domain;
        }
    }

    @Data
    public static class UserItem {
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
        @NotEmpty
        private String externalId;
        private Map<String, Object> properties = Collections.emptyMap();

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

    @Data
    public static class AuthorityItem {
        @NotEmpty
        private String identifier;
        @NotEmpty
        private String name;
        @NotEmpty
        private String description;

        public Authority toEntity(String domainId) {
            Authority auth = new Authority();
            auth.setDomainId(domainId);
            auth.setDescription(description);
            auth.setName(name);
            auth.setIdentifier(identifier);
            return auth;
        }
    }

    @Data
    public static class GroupItem {
        @NotEmpty
        private String identifier;
        @NotEmpty
        private String name;
        @NotEmpty
        private String description;
        private String parent;

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

    @Data
    public static class RoleItem {
        @NotEmpty
        private String identifier;
        @NotEmpty
        private String name;
        @NotEmpty
        private String description;

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

    @Data
    public static class RoleAuthorityItem {
        @NotEmpty
        private String role;
        @NotEmpty
        private String authority;

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

    @Data
    public static class UserAuthorityItem {
        @NotEmpty
        private String user;
        @NotEmpty
        private String authority;

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

    @Data
    public static class UserGroupItem {
        @NotEmpty
        private String user;
        @NotEmpty
        private String group;

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

    @Data
    public static class UserRoleItem {
        @NotEmpty
        private String user;
        @NotEmpty
        private String role;

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
