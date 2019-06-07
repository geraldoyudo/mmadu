package com.mmadu.service.services;

import com.mmadu.service.entities.AppUser;
import com.mmadu.service.exceptions.DomainNotFoundException;
import com.mmadu.service.exceptions.DuplicationException;
import com.mmadu.service.exceptions.UserNotFoundException;
import com.mmadu.service.model.UserView;
import com.mmadu.service.models.PagedList;
import com.mmadu.service.providers.UniqueUserIdGenerator;
import com.mmadu.service.repositories.AppDomainRepository;
import com.mmadu.service.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Optional;

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
        if (appUserRepository.existsByUsernameAndDomainId(userView.getUsername(), domainId) ||
            appUserRepository.existsByExternalIdAndDomainId(userView.getId(), domainId)) {
            throw new DuplicationException("user already exists");
        }
        if(StringUtils.isEmpty(userView.getId())){
            userView.setId(uniqueUserIdGenerator.generateUniqueId(domainId));
        }
        if(StringUtils.isEmpty(userView.getId())){
            userView.setId(uniqueUserIdGenerator.generateUniqueId(domainId));
        }
        AppUser appUser = new AppUser(domainId, userView);
        appUserRepository.save(appUser);
    }

    @Override
    public Page<UserView> getAllUsers(String domainId, Pageable pageable) {
        if (!appDomainRepository.existsById(domainId)) {
            throw new DomainNotFoundException();
        }
        Page<UserView> userViewPage = appUserRepository.findByDomainId(domainId, pageable)
                .map(AppUser::userView);
        return new PagedList<>(userViewPage.getContent(), userViewPage.getPageable(), userViewPage.getTotalElements());
    }

    @Override
    public UserView getUserByDomainIdAndExternalId(String domainId, String externalId) {
        if (!appDomainRepository.existsById(domainId)) {
            throw new DomainNotFoundException();
        }
        AppUser user = appUserRepository.findByDomainIdAndExternalId(domainId, externalId)
                .orElseThrow(UserNotFoundException::new);
        return user.userView();
    }
}
