package com.mmadu.service.services;

import com.mmadu.service.config.DatabaseConfig;
import com.mmadu.service.entities.AppUser;
import com.mmadu.service.entities.Authority;
import com.mmadu.service.entities.UserAuthority;
import com.mmadu.service.models.AuthorityData;
import com.mmadu.service.repositories.AppUserRepository;
import com.mmadu.service.repositories.AuthorityRepository;
import com.mmadu.service.repositories.UserAuthorityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Import({
        AuthorityManagementServiceImpl.class,
        DatabaseConfig.class
})
class AuthorityManagementServiceImplTest {
    static final String DOMAIN_ID = "test-domain";
    public static final String EXTERNAL_ID = "38923820";
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private UserAuthorityRepository userAuthorityRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AuthorityManagementService authorityManagementService;

    @AfterEach
    void clear() {
        authorityRepository.deleteAll();
        userAuthorityRepository.deleteAll();
        appUserRepository.deleteAll();
    }

    @Test
    void saveAuthorities() {
        List<AuthorityData> authorities = asList(
                authority("view", "Name View", " Description View").authorityData(),
                authority("edit", "Name Edit", "Description Edit").authorityData()
        );
        authorityManagementService.saveAuthorities(DOMAIN_ID, authorities);
        assertAll(
                () -> assertEquals(2, authorityRepository.findAll().size()),
                () -> assertTrue(authorityRepository.findByDomainIdAndIdentifier(DOMAIN_ID, "view").isPresent()),
                () -> assertTrue(authorityRepository.findByDomainIdAndIdentifier(DOMAIN_ID, "edit").isPresent())
        );
    }

    private Authority authority(String identifier, String name, String description) {
        Authority auth = new Authority();
        auth.setName(name);
        auth.setIdentifier(identifier);
        auth.setDescription(description);
        auth.setDomainId(DOMAIN_ID);
        return auth;
    }


    @Test
    void getAuthorities() {
        createSampleAuthorities();
        Page<AuthorityData> authorities = authorityManagementService.getAuthorities(DOMAIN_ID, Pageable.unpaged());
        assertAll(
                () -> assertEquals(2, authorities.getTotalElements()),
                () -> assertEquals("view", authorities.getContent().get(0).getIdentifier()),
                () -> assertEquals("edit", authorities.getContent().get(1).getIdentifier())
        );
    }

    private List<Authority> createSampleAuthorities() {
        return authorityRepository.saveAll(
                List.of(
                        authority("view", "View Name", "View Description"),
                        authority("edit", "Edit Name", "Edit Description")
                )
        );
    }

    @Test
    void deleteAuthority() {
        createSampleAuthorities();
        ;

        authorityManagementService.deleteAuthority(DOMAIN_ID, "view");

        assertEquals(1, authorityRepository.count());
    }

    @Test
    void grantUserAuthorities() {
        AppUser user = createSampleUser();
        List<Authority> authorities = createSampleAuthorities();
        authorityManagementService.grantUserAuthorities(DOMAIN_ID, EXTERNAL_ID,
                List.of("view", "edit"));
        assertAll(
                () -> assertEquals(2, userAuthorityRepository.count()),
                () -> assertTrue(userAuthorityRepository.existsByDomainIdAndUserIdAndAuthorityId(DOMAIN_ID, user.getId(), authorities.get(0).getId())),
                () -> assertTrue(userAuthorityRepository.existsByDomainIdAndUserIdAndAuthorityId(DOMAIN_ID, user.getId(), authorities.get(1).getId()))
        );
    }

    private AppUser createSampleUser() {
        AppUser user = new AppUser();
        user.setUsername("user");
        user.setPassword("password");
        user.setExternalId(EXTERNAL_ID);
        user.setDomainId(DOMAIN_ID);
        return appUserRepository.save(user);
    }

    @Test
    void revokeUserAuthority() {
        AppUser user = createSampleUser();
        List<Authority> authorities = createSampleAuthorities();
        grantUserAuthorities(user, authorities);

        authorityManagementService.revokeUserAuthorities(DOMAIN_ID, EXTERNAL_ID, Collections.singletonList("view"));

        assertAll(
                () -> assertFalse(userAuthorityRepository.existsByDomainIdAndUserIdAndAuthorityId(DOMAIN_ID, user.getId(), authorities.get(0).getId())),
                () -> assertTrue(userAuthorityRepository.existsByDomainIdAndUserIdAndAuthorityId(DOMAIN_ID, user.getId(), authorities.get(1).getId()))
        );
    }

    private void grantUserAuthorities(AppUser user, List<Authority> authorities) {
        authorities.forEach(auth -> {
            UserAuthority userAuth = new UserAuthority();
            userAuth.setDomainId(DOMAIN_ID);
            userAuth.setUser(user);
            userAuth.setAuthority(auth);
            userAuthorityRepository.save(userAuth);
        });
    }

    @Test
    void getUserAuthorities() {
        AppUser user = createSampleUser();
        List<Authority> authorities = createSampleAuthorities();
        grantUserAuthorities(user, authorities);

        assertEquals(
                Set.of("view", "edit"),
                authorityManagementService.getUserAuthorities(DOMAIN_ID, EXTERNAL_ID)
        );
    }
}