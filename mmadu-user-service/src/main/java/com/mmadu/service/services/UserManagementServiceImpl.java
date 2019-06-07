package com.mmadu.service.services;

import com.mmadu.service.entities.AppUser;
import com.mmadu.service.exceptions.DomainNotFoundException;
import com.mmadu.service.exceptions.DuplicationException;
import com.mmadu.service.model.UserView;
import com.mmadu.service.providers.UniqueUserIdGenerator;
import com.mmadu.service.repositories.AppDomainRepository;
import com.mmadu.service.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserManagementServiceImpl implements UserManagementService {
    private AppUserRepository appUserRepository;
    private AppDomainRepository appDomainRepository;
    private UniqueUserIdGenerator uniqueUserIdGenerator;

    @Autowired
    public void setAppUserRepository(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Autowired
    public void setAppDomainRepository(AppDomainRepository appDomainRepository) {
        this.appDomainRepository = appDomainRepository;
    }

    @Autowired
    public void setUniqueUserIdGenerator(UniqueUserIdGenerator uniqueUserIdGenerator) {
        this.uniqueUserIdGenerator = uniqueUserIdGenerator;
    }

    @Override
    public void createUser(String domainId, UserView userView) {
        if (userView == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
        if (StringUtils.isEmpty(userView.getUsername())) {
            throw new IllegalArgumentException("user missing username");
        }
        if (userView.getPassword() == null) {
            throw new IllegalArgumentException("user missing password");
        }
        if (!appDomainRepository.existsById(domainId)) {
            throw new DomainNotFoundException();
        }
        if (appUserRepository.existsByUsernameAndDomainId(userView.getUsername(), domainId)) {
            throw new DuplicationException("user already exists");
        }
        if(StringUtils.isEmpty(userView.getId())){
            userView.setId(uniqueUserIdGenerator.generateUniqueId(domainId));
        }
        AppUser appUser = new AppUser(domainId, userView);
        appUserRepository.save(appUser);
    }
}
