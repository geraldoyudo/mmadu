package com.mmadu.service.services;

import com.mmadu.service.config.DatabaseConfig;
import com.mmadu.service.entities.AppUser;
import com.mmadu.service.entities.Group;
import com.mmadu.service.entities.UserGroup;
import com.mmadu.service.models.GroupUserRemovalRequest;
import com.mmadu.service.models.NewGroupRequest;
import com.mmadu.service.models.NewGroupUserRequest;
import com.mmadu.service.repositories.AppUserRepository;
import com.mmadu.service.repositories.GroupRepository;
import com.mmadu.service.repositories.UserGroupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Import({
        DatabaseConfig.class,
        GroupServiceImpl.class
})
class GroupServiceImplTest {
    public static final String DOMAIN_ID = "0";
    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;
    private AppUser user;
    private Group group;
    private UserGroup userGroup;
    private Group ancestor;
    private AppUser another;

    @AfterEach
    void clear() {
        userGroupRepository.deleteAll();
        groupRepository.deleteAll();
        appUserRepository.deleteAll();
    }

    @Test
    void addGroup() {
        NewGroupRequest request = new NewGroupRequest();
        request.setDescription("test-description");
        request.setIdentifier("test-id");
        request.setName("test");

        groupService.addGroup(DOMAIN_ID, request);

        assertTrue(groupRepository.findByDomainIdAndIdentifier(DOMAIN_ID, request.getIdentifier()).isPresent());
    }

    @Test
    void addUserToGroup() {
        AppUser user = new AppUser();
        user.setDomainId(DOMAIN_ID);
        user.setPassword("1234");
        user.setUsername("user");
        user.setExternalId("2323");
        user = appUserRepository.save(user);

        Group group = new Group();
        group.setDomainId(DOMAIN_ID);
        group.setIdentifier("sample");
        group.setDescription("sample");
        group.setName("sample");
        group = groupRepository.save(group);
        NewGroupUserRequest request = new NewGroupUserRequest();
        request.setGroup(group.getIdentifier());
        request.setId(user.getExternalId());
        groupService.addUserToGroup(DOMAIN_ID, request);

        assertTrue(
                userGroupRepository.findByUserId(user.getId()).size() == 1
        );
    }

    @Test
    void removeUserFromGroup() {
        createUserAndAddToGroup();
        GroupUserRemovalRequest request = new GroupUserRemovalRequest();
        request.setGroup(group.getIdentifier());
        request.setId(user.getExternalId());
        groupService.removeUserFromGroup(DOMAIN_ID, request);
        assertTrue(userGroupRepository.findById(userGroup.getId()).isEmpty());
    }

    private void createUserAndAddToGroup() {
        user = new AppUser();
        user.setDomainId(DOMAIN_ID);
        user.setPassword("1234");
        user.setUsername("user");
        user.setExternalId("2323");
        user = appUserRepository.save(user);

        group = new Group();
        group.setDomainId(DOMAIN_ID);
        group.setIdentifier("sample");
        group.setDescription("sample");
        group.setName("sample");
        group = groupRepository.save(group);

        userGroup = new UserGroup();
        userGroup.setUser(user);
        userGroup.setGroup(group);
        userGroup.setDomainId(DOMAIN_ID);
        userGroup = userGroupRepository.save(userGroup);
    }

    @Test
    void getGroups() {
        createUserAndAddToGroup();
        addAncestorToGroup();
        assertEquals(
                Set.of("ancestor", "sample"),
                groupService.getGroups(DOMAIN_ID, user.getExternalId())
        );
    }

    private void addAncestorToGroup() {
        ancestor = new Group();
        ancestor.setName("ancestor");
        ancestor.setDescription("ancestor");
        ancestor.setIdentifier("ancestor");
        ancestor.setDomainId(DOMAIN_ID);
        ancestor = groupRepository.save(ancestor);
        group.setParent(ancestor);
        groupRepository.save(ancestor);
        group = groupRepository.save(group);
    }

    @Test
    void getUsersInGroup() {
        createUserAndAddToGroup();
        addAncestorToGroup();
        addAnotherUserToAncestorGroup();

        assertAll(
                () -> assertEquals(1, groupService.getUsersInGroup(DOMAIN_ID,
                        group.getIdentifier(), Pageable.unpaged()).getTotalElements()),
                () -> assertEquals(2, groupService.getUsersInGroup(DOMAIN_ID,
                        ancestor.getIdentifier(), Pageable.unpaged()).getTotalElements())
        );
    }

    private void addAnotherUserToAncestorGroup() {
        another = new AppUser();
        another.setUsername("another");
        another.setPassword("1323");
        another.setDomainId(DOMAIN_ID);
        another.setExternalId("389283");
        another = appUserRepository.save(another);

        UserGroup userGroup = new UserGroup();
        userGroup.setGroup(ancestor);
        userGroup.setDomainId(DOMAIN_ID);
        userGroup.setUser(another);
        userGroupRepository.save(userGroup);
    }

    @Test
    void userInGroup() {
        createUserAndAddToGroup();
        addAncestorToGroup();
        addAnotherUserToAncestorGroup();

        assertAll(
                () -> assertTrue(groupService.userInGroup(DOMAIN_ID, user.getExternalId(), group.getIdentifier())),
                () -> assertTrue(groupService.userInGroup(DOMAIN_ID, user.getExternalId(), ancestor.getIdentifier())),
                () -> assertFalse(groupService.userInGroup(DOMAIN_ID, another.getExternalId(), group.getIdentifier())),
                () -> assertTrue(groupService.userInGroup(DOMAIN_ID, another.getExternalId(), ancestor.getIdentifier()))

        );
    }
}