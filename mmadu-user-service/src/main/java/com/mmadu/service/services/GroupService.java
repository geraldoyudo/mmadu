package com.mmadu.service.services;

import com.mmadu.service.models.GroupUserRemovalRequest;
import com.mmadu.service.models.NewGroupRequest;
import com.mmadu.service.models.NewGroupUserRequest;
import com.mmadu.service.models.UserView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupService {

    void addGroup(String domainId, NewGroupRequest request);

    void addUserToGroup(String domainId, NewGroupUserRequest request);

    void removeUserFromGroup(String domainId, GroupUserRemovalRequest request);

    List<String> getGroups(String domainId, String userId);

    Page<UserView> getUsersInGroup(String domainId, String group, Pageable pageable);

    boolean userInGroup(String domainId, String userExternalId, String groupIdentifier);
}
