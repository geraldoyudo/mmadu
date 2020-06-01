package com.mmadu.identity.services.user;

import com.mmadu.identity.models.user.MmaduUser;

import java.util.Optional;

public interface MmaduUserService {

    Optional<MmaduUser> loadUserByUsernameAndDomainId(String username, String domainId);

    void authenticate(String domainId, String username, String password);
}
