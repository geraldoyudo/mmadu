package com.mmadu.identity.providers.users;

import com.mmadu.identity.models.users.MmaduUser;
import com.mmadu.identity.models.users.MmaduUserImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MmaduUserServiceImpl implements MmaduUserService {
    @Override
    public Optional<MmaduUser> loadUserByUsername(String username) {
        if("gerald".equals(username)){
            MmaduUserImpl user = new MmaduUserImpl();
            user.setId("1234");
            user.setAuthorities(List.of("view", "edit"));
            user.setDomainId("0");
            user.setRoles(List.of("one"));
            user.setUsername("gerald");
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public void authenticate(String domainId, String username, String password) {

    }
}
