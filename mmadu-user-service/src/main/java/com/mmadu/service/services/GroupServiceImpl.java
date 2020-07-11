package com.mmadu.service.services;

import com.mmadu.service.entities.AppUser;
import com.mmadu.service.entities.Group;
import com.mmadu.service.entities.UserGroup;
import com.mmadu.service.exceptions.DuplicationException;
import com.mmadu.service.exceptions.GroupNotFoundException;
import com.mmadu.service.exceptions.NotFoundException;
import com.mmadu.service.exceptions.UserNotFoundException;
import com.mmadu.service.models.*;
import com.mmadu.service.repositories.AppUserRepository;
import com.mmadu.service.repositories.GroupRepository;
import com.mmadu.service.repositories.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {
    private GroupRepository groupRepository;
    private UserGroupRepository userGroupRepository;
    private AppUserRepository appUserRepository;

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Autowired
    public void setUserGroupRepository(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    @Autowired
    public void setAppUserRepository(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    @Transactional
    public void addGroup(String domainId, NewGroupRequest request) {
        if (groupRepository.existsByDomainIdAndIdentifier(domainId, request.getIdentifier())) {
            throw new DuplicationException("group already exists");
        }
        Group group = new Group();
        group.setIdentifier(request.getIdentifier());
        group.setDescription(request.getDescription());
        group.setName(request.getName());
        group.setDomainId(domainId);
        if (!StringUtils.isEmpty(request.getParentGroup())) {
            group.setParent(
                    groupRepository.findByDomainIdAndIdentifier(domainId, request.getParentGroup())
                            .orElseThrow(GroupNotFoundException::new)
            );
        }
        groupRepository.save(group);
    }

    @Override
    @Transactional
    public void addUserToGroup(String domainId, NewGroupUserRequest request) {
        AppUser user = appUserRepository.findByDomainIdAndExternalId(domainId, request.getId())
                .orElseThrow(UserNotFoundException::new);
        Group group = groupRepository.findByDomainIdAndIdentifier(domainId, request.getGroup())
                .orElseThrow(GroupNotFoundException::new);
        if (userGroupRepository.existsByDomainIdAndUserIdAndGroupId(domainId, user.getId(), group.getId())) {
            throw new DuplicationException("user is already in group");
        }
        UserGroup userGroup = new UserGroup();
        userGroup.setDomainId(domainId);
        userGroup.setGroup(
                groupRepository.findByDomainIdAndIdentifier(domainId, request.getGroup())
                        .orElseThrow(GroupNotFoundException::new)
        );
        userGroup.setUser(
                appUserRepository.findByDomainIdAndExternalId(domainId, request.getId())
                        .orElseThrow(UserNotFoundException::new)
        );
        userGroupRepository.save(userGroup);
    }

    @Override
    @Transactional
    public void removeUserFromGroup(String domainId, GroupUserRemovalRequest request) {
        AppUser user = appUserRepository.findByDomainIdAndExternalId(domainId, request.getId())
                .orElseThrow(UserNotFoundException::new);
        Group group = groupRepository.findByDomainIdAndIdentifier(domainId, request.getGroup())
                .orElseThrow(GroupNotFoundException::new);

        if (!userGroupRepository.existsByDomainIdAndUserIdAndGroupId(domainId, user.getId(), group.getId())) {
            throw new NotFoundException("user is not directly linked to group");
        }

        userGroupRepository.deleteByDomainIdAndUserIdAndGroupId(domainId, user.getId(), group.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> getGroups(String domainId, String userId) {
        AppUser user = appUserRepository.findByDomainIdAndExternalId(domainId, userId)
                .orElseThrow(UserNotFoundException::new);
        List<Group> groups = userGroupRepository.findByDomainIdAndUserId(domainId, user.getId())
                .stream()
                .map(UserGroup::getGroup)
                .collect(Collectors.toList());
        Set<String> groupIdentifiers = new HashSet<>();
        for (Group group : groups) {
            groupIdentifiers.add(group.getIdentifier());
            addParents(group, groupIdentifiers);
        }
        return groupIdentifiers;
    }

    private void addParents(Group group, Set<String> groupIdentifiers) {
        if (group.getParent() != null) {
            groupIdentifiers.add(group.getParent().getIdentifier());
            addParents(group.getParent(), groupIdentifiers);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserView> getUsersInGroup(String domainId, String groupIdentifier, Pageable pageable) {
        Group group = groupRepository.findByDomainIdAndIdentifier(domainId, groupIdentifier)
                .orElseThrow(GroupNotFoundException::new);
        List<Group> groups = getGroupAndChildren(group);
        Page<UserView> userViewPage = userGroupRepository.findByDomainIdAndGroupIn(domainId, groups, pageable)
                .map(ug -> ug.getUser().userView());
        return new PagedList<>(userViewPage.getContent(), userViewPage.getPageable(), userViewPage.getTotalElements());

    }

    private List<Group> getGroupAndChildren(Group group) {
        List<Group> groups = new LinkedList<>();
        addGroupToList(group, groups);
        return groups;
    }

    private void addGroupToList(Group group, List<Group> groups) {
        groups.add(group);
        if (group.getChildren() != null && !group.getChildren().isEmpty()) {
            group.getChildren().forEach(
                    child -> addGroupToList(child, groups)
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userInGroup(String domainId, String userExternalId, String groupIdentifier) {
        Group group = groupRepository.findByDomainIdAndIdentifier(domainId, groupIdentifier)
                .orElseThrow(GroupNotFoundException::new);
        AppUser user = appUserRepository.findByDomainIdAndExternalId(domainId, userExternalId)
                .orElseThrow(UserNotFoundException::new);
        List<Group> groupAndChildren = getGroupAndChildren(group);
        return userGroupRepository.existsByDomainIdAndUserIdAndGroupIn(
                domainId, user.getId(), groupAndChildren
        );
    }
}
