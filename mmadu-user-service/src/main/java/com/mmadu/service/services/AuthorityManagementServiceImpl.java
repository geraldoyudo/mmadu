package com.mmadu.service.services;

import com.mmadu.service.entities.AppUser;
import com.mmadu.service.entities.Authority;
import com.mmadu.service.entities.UserAuthority;
import com.mmadu.service.exceptions.NotFoundException;
import com.mmadu.service.exceptions.UserNotFoundException;
import com.mmadu.service.models.AuthorityData;
import com.mmadu.service.models.PagedList;
import com.mmadu.service.repositories.AppUserRepository;
import com.mmadu.service.repositories.AuthorityRepository;
import com.mmadu.service.repositories.UserAuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorityManagementServiceImpl implements AuthorityManagementService {
    private AuthorityRepository authorityRepository;
    private AppUserRepository appUserRepository;
    private UserAuthorityRepository userAuthorityRepository;

    @Autowired
    public void setAuthorityRepository(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Autowired
    public void setAppUserRepository(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Autowired
    public void setUserAuthorityRepository(UserAuthorityRepository userAuthorityRepository) {
        this.userAuthorityRepository = userAuthorityRepository;
    }

    @Override
    public void saveAuthorities(String domainId, List<AuthorityData> authorities) {
        authorities
                .forEach(auth -> createOrSave(domainId, auth));
    }

    private void createOrSave(String domainId, AuthorityData auth) {
        authorityRepository.findByDomainIdAndIdentifier(domainId, auth.getIdentifier())
                .ifPresentOrElse(oldAuth -> updateAuth(oldAuth, auth), () -> createAuth(domainId, auth));
    }

    private void updateAuth(Authority oldAuth, AuthorityData data) {
        oldAuth.setDescription(data.getDescription());
        oldAuth.setIdentifier(data.getIdentifier());
        oldAuth.setName(data.getName());
        authorityRepository.save(oldAuth);
    }

    private void createAuth(String domainId, AuthorityData data) {
        Authority authority = new Authority();
        authority.setDomainId(domainId);
        authority.setName(data.getName());
        authority.setIdentifier(data.getIdentifier());
        authority.setDescription(data.getDescription());
        authorityRepository.save(authority);
    }

    @Override
    public Page<AuthorityData> getAuthorities(String domainId, Pageable p) {
        Page<AuthorityData> authoritiesPage = authorityRepository.findByDomainId(domainId, p)
                .map(Authority::authorityData);
        return new PagedList<>(authoritiesPage.getContent(), authoritiesPage.getPageable(), authoritiesPage.getTotalElements());
    }

    @Override
    public void deleteAuthority(String domainId, String identifier) {
        if (!authorityRepository.existsByDomainIdAndIdentifier(domainId, identifier)) {
            throw new NotFoundException("authority not found");
        }
        authorityRepository.deleteByDomainIdAndIdentifier(domainId, identifier);
    }

    @Override
    public void grantUserAuthorities(String domainId, String userId, List<String> authorityIdentifiers) {
        AppUser user = appUserRepository.findByDomainIdAndExternalId(domainId, userId)
                .orElseThrow(UserNotFoundException::new);
        List<Authority> authorities = authorityRepository.findByDomainIdAndIdentifierIn(domainId, authorityIdentifiers);
        if (authorities.size() != authorityIdentifiers.size()) {
            throw new NotFoundException("some or all authorities could not be found");
        }
        authorities.forEach(auth -> addUserAuthIfNotExists(domainId, user, auth));
    }

    private void addUserAuthIfNotExists(String domainId, AppUser user, Authority auth) {
        if (!userAuthorityRepository.existsByDomainIdAndUserIdAndAuthorityId(domainId, user.getId(), auth.getId())) {
            UserAuthority userAuthority = new UserAuthority();
            userAuthority.setUser(user);
            userAuthority.setAuthority(auth);
            userAuthority.setDomainId(domainId);
            userAuthorityRepository.save(userAuthority);
        }
    }

    @Override
    public void revokeUserAuthorities(String domainId, String userId, List<String> authorityIdentifiers) {
        AppUser user = appUserRepository.findByDomainIdAndExternalId(domainId, userId)
                .orElseThrow(UserNotFoundException::new);
        List<Authority> authorities = authorityRepository.findByDomainIdAndIdentifierIn(domainId, authorityIdentifiers);
        if (authorities.size() != authorityIdentifiers.size()) {
            throw new NotFoundException("some or all authorities could not be found");
        }
        authorities.forEach(auth -> removeUserAuthIfExists(domainId, user, auth));
    }

    private void removeUserAuthIfExists(String domainId, AppUser user, Authority auth) {
        if (userAuthorityRepository.existsByDomainIdAndUserIdAndAuthorityId(domainId, user.getId(), auth.getId())) {
            userAuthorityRepository.deleteByDomainIdAndUserIdAndAuthorityId(domainId, user.getId(), auth.getId());
        }
    }

    @Override
    public Set<String> getUserAuthorities(String domainId, String userId) {
        AppUser user = appUserRepository.findByDomainIdAndExternalId(domainId, userId)
                .orElseThrow(UserNotFoundException::new);
        return userAuthorityRepository.findByDomainIdAndUserId(domainId, user.getId())
                .stream()
                .map(userAuthority -> userAuthority.getAuthority().getIdentifier())
                .collect(Collectors.toSet());
    }
}
