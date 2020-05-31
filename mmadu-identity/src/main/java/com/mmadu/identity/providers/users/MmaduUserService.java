package com.mmadu.identity.providers.users;

import com.mmadu.identity.models.users.MmaduUser;

import java.util.Optional;

public interface MmaduUserService {

    Optional<MmaduUser> loadUserByUsernameAndDomainId(String username, String domainId);

    void authenticate(String domainId, String username, String password);
}
