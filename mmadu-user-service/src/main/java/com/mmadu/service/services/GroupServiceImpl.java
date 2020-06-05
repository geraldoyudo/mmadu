package com.mmadu.service.services;

import com.mmadu.service.entities.Group;
import com.mmadu.service.entities.UserGroup;
import com.mmadu.service.exceptions.DuplicationException;
import com.mmadu.service.exceptions.GroupNotFoundException;
import com.mmadu.service.exceptions.NotFoundException;
import com.mmadu.service.exceptions.UserNotFoundException;
import com.mmadu.service.models.GroupUserRemovalRequest;
import com.mmadu.service.models.NewGroupRequest;
import com.mmadu.service.models.NewGroupUserRequest;
import com.mmadu.service.models.UserView;
import com.mmadu.service.repositories.AppUserRepository;
import com.mmadu.service.repositories.GroupRepository;
import com.mmadu.service.repositories.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
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
    public void addUserToGroup(String domainId, NewGroupUserRequest request) {
        if (userGroupRepository.existsByDomainIdAndUserExternalIdAndGroupIdentifier(domainId, request.getId(), request.getGroup())) {
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
    public void removeUserFromGroup(String domainId, GroupUserRemovalRequest request) {
        if (!userGroupRepository.existsByDomainIdAndUserExternalIdAndGroupIdentifier(domainId, request.getId(), request.getGroup())) {
            throw new NotFoundException("user is not directly linked to group");
        }
    }

    @Override
    public List<String> getGroups(String domainId, String userId) {
        List<Group> groups = userGroupRepository.findByDomainIdAndUserExternalId(domainId, userId)
                .stream()
                .map(UserGroup::getGroup)
                .collect(Collectors.toList());
        Set<String> groupIdentifiers = new HashSet<>();
        for (Group group : groups) {
            groupIdentifiers.add(group.getIdentifier());
            addParents(group, groupIdentifiers);
        }
        return new ArrayList<>(groupIdentifiers);
    }

    private void addParents(Group group, Set<String> groupIdentifiers) {
        if (group.getParent() != null) {
            groupIdentifiers.add(group.getParent().getIdentifier());
            addParents(group.getParent(), groupIdentifiers);
        }
    }

    @Override
    public Page<UserView> getUsersInGroup(String domainId, String groupIdentifier, Pageable pageable) {
        Group group = groupRepository.findByDomainIdAndIdentifier(domainId, groupIdentifier)
                .orElseThrow(GroupNotFoundException::new);
        List<String> groupIds = new LinkedList<>();
        addGroupToList(group, groupIds);
        return null;
    }

    private void addGroupToList(Group group, List<String> groupIds) {
        groupIds.add(group.getIdentifier());
        if (group.getChildren() != null && !group.getChildren().isEmpty()) {
            group.getChildren().forEach(
                    child -> addGroupToList(child, groupIds)
            );
        }
    }

    @Override
    public boolean userInGroup(String domainId, String userExternalId, String groupIdentifier) {
        return false;


    }
}
