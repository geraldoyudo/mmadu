package com.mmadu.service.repositories;


import com.geraldoyudo.kweeri.core.expression.Expression;
import com.geraldoyudo.kweeri.mongo.MongoQueryConverter;
import com.geraldoyudo.kweeri.mongo.MongoQuerySerializer;
import com.mmadu.service.config.DatabaseConfig;
import com.mmadu.service.entities.AppUser;
import com.mmadu.service.models.DomainIdObject;
import com.mmadu.service.models.PatchOperation;
import com.mmadu.service.models.UpdateRequest;
import com.mmadu.service.models.UserPatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
@Import(DatabaseConfig.class)
class AppUserRepositoryTest {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        appUserRepository.deleteAll();
    }

    @Test
    void createAndRetrieveAppUser() {
        AppUser user = new AppUser();
        user.setUsername("user");
        user.setPassword("password");
        user = appUserRepository.save(user);
        assertThat(user.getId(), notNullValue());
        AppUser retrievedUser = appUserRepository.findById(user.getId()).get();
        assertThat(retrievedUser, notNullValue());
    }

    @Test
    void findByUserNameAndDomain() {
        initializeAppUser();
        AppUser user = appUserRepository.findByUsernameAndDomainId("user", "test").get();
        assertThat(user, notNullValue());
    }

    private AppUser initializeAppUser() {
        AppUser createdUser = createAppUser();
        return appUserRepository.save(createdUser);
    }

    private AppUser createAppUser() {
        AppUser createdUser = new AppUser();
        createdUser.setUsername("user");
        createdUser.setPassword("password");
        createdUser.setDomainId("test");
        createdUser.setExternalId("ext-id");
        return createdUser;
    }

    @Test
    void findDomainIdForUser() {
        AppUser appUser = initializeAppUser();
        DomainIdObject domainId = appUserRepository.findDomainIdForUser(appUser.getId()).get();
        assertThat(domainId.getDomainId(), equalTo(appUser.getDomainId()));
    }

    @Test
    void existsByUserNameAndDomain() {
        initializeAppUser();
        boolean exists = appUserRepository.existsByUsernameAndDomainId("user", "test");
        assertThat(exists, is(true));
    }

    @Test
    void existsByExternalId() {
        initializeAppUser();
        boolean exists = appUserRepository.existsByExternalIdAndDomainId("ext-id", "test");
        assertThat(exists, is(true));
    }

    @Test
    @Disabled("test fails unexpectedly. Fix soon.")
    void whenAddTwoUsersWithSameExternalIdThrowException() {
        AppUser appUser1 = createAppUser();
        AppUser appUser2 = createAppUser();
        appUser2.setUsername("user-2");
        appUserRepository.save(appUser1);
        assertThrows(DuplicateKeyException.class, () -> appUserRepository.save(appUser2));
    }

    @Test
    @Disabled("test fails unexpectedly. Fix soon.")
    void whenAddTwoUsersWithSameUsernameThrowException() {
        AppUser appUser1 = createAppUser();
        AppUser appUser2 = createAppUser();
        appUser2.setExternalId("ext-id-232");
        appUserRepository.save(appUser1);
        assertThrows(DuplicateKeyException.class, () -> appUserRepository.save(appUser2));
    }

    @Test
    void findByDomainAndExternalId() {
        initializeAppUser();
        AppUser user = appUserRepository.findByDomainIdAndExternalId("test", "ext-id").get();
        assertThat(user, notNullValue());
    }

    @Test
    void testQueriesWithBasicQuery() {
        AppUser user = createAppUser();
        user.set("color", "red");
        appUserRepository.save(user);
        MongoQueryConverter converter = new MongoQueryConverter() {
            @Override
            protected String transformProperty(String property) {
                if (!property.equals("username")) {
                    return "properties." + property;
                } else {
                    return property;
                }
            }
        };
        MongoQuerySerializer querySerializer = new MongoQuerySerializer();
        Expression expression = querySerializer.deSerialize("color equals 'red'");
        String jsonQuery = converter.convertExpressionToQuery(expression);
        BasicQuery query = new BasicQuery(jsonQuery);
        List<AppUser> userList = mongoTemplate.find(query, AppUser.class);
        assertThat(userList.size(), equalTo(1));
    }

    @Test
    void queryUsers() {
        AppUser user = createAppUser();
        user.set("color", "red");
        appUserRepository.save(user);
        AppUser user1 = createAppUser();
        user1.set("color", "blue");
        user1.setExternalId("new-ext");
        user1.setUsername("new-user");
        appUserRepository.save(user1);
        PageRequest request = PageRequest.of(0, 10);
        Page<AppUser> appUsers = appUserRepository.queryForUsers("(color equals 'red') " +
                "and (username equals 'user') and ('domainId' equals 'test')", request);
        assertThat(appUsers.getContent().size(), equalTo(1));
        assertThat(appUsers.getTotalElements(), equalTo(1L));
    }

    @Test
    void updateUsers() {
        AppUser user = createAppUser();
        user.set("color", "red");
        user.set("amount", 10);
        user.set("list", new ArrayList<String>());
        user.set("set", asList("one"));
        appUserRepository.save(user);
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.addUpdate(new UserPatch(PatchOperation.SET, "color", "blue"));
        updateRequest.addUpdate(new UserPatch(PatchOperation.INCREMENT, "amount", 10));
        updateRequest.addUpdate(new UserPatch(PatchOperation.ADD, "list", "one"));
        updateRequest.addUpdate(new UserPatch(PatchOperation.REMOVE, "set", "one"));
        appUserRepository.updateUsers("(color equals 'red') " +
                "and (username equals 'user') and ('domainId' equals 'test')", updateRequest);
        AppUser updatedUser = appUserRepository.findById(user.getId()).get();
        assertThat(updatedUser.get("color").orElse(""), equalTo("blue"));
        assertThat(updatedUser.get("amount").orElse(0), equalTo(20));
        assertThat((List<String>) updatedUser.get("list").orElse(Collections.emptyList()), contains("one"));
        assertThat(((List<String>) updatedUser.get("set").orElse(Collections.emptyList())).size(), equalTo(0));
    }
}