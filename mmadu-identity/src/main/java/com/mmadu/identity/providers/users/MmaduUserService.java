package com.mmadu.identity.providers.users;

import com.mmadu.identity.models.users.MmaduUser;

import java.util.Optional;

public interface MmaduUserService {

    Optional<MmaduUser> loadUserByUsername(String username);

    void authenticate(String domainId, String username, String password);
}
