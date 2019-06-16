package com.mmadu.service.repositories;


import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import com.geraldoyudo.kweeri.core.expression.Expression;
import com.geraldoyudo.kweeri.mongo.MongoQueryConverter;
import com.geraldoyudo.kweeri.mongo.MongoQuerySerializer;
import com.mmadu.service.config.KweeriConfig;
import com.mmadu.service.entities.AppUser;
import com.mmadu.service.models.DomainIdObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
@Import(KweeriConfig.class)
public class AppUserRepositoryTest {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void setUp(){
        appUserRepository.deleteAll();
    }

    @Test
    public void createAndRetrieveAppUser(){
        AppUser user = new AppUser();
        user.setUsername("user");
        user.setPassword("password");
        user = appUserRepository.save(user);
        assertThat(user.getId(), notNullValue());
        AppUser retrievedUser = appUserRepository.findById(user.getId()).get();
        assertThat(retrievedUser, notNullValue());
    }

    @Test
    public void findByUserNameAndDomain(){
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
    public void findDomainIdForUser(){
        AppUser appUser = initializeAppUser();
        DomainIdObject domainId = appUserRepository.findDomainIdForUser(appUser.getId()).get();
        assertThat(domainId.getDomainId(), equalTo(appUser.getDomainId()));
    }

    @Test
    public void existsByUserNameAndDomain(){
        initializeAppUser();
        boolean exists = appUserRepository.existsByUsernameAndDomainId("user", "test");
        assertThat(exists, is(true));
    }

    @Test
    public void existsByExternalId(){
        initializeAppUser();
        boolean exists = appUserRepository.existsByExternalIdAndDomainId("ext-id", "test");
        assertThat(exists, is(true));
    }

    @Test(expected = DuplicateKeyException.class)
    public void whenAddTwoUsersWithSameExternalIdThrowException(){
        AppUser appUser1 = createAppUser();
        AppUser appUser2 = createAppUser();
        appUser2.setUsername("user-2");
        appUserRepository.save(appUser1);
        appUserRepository.save(appUser2);
    }

    @Test(expected = DuplicateKeyException.class)
    public void whenAddTwoUsersWithSameUsernameThrowException(){
        AppUser appUser1 = createAppUser();
        AppUser appUser2 = createAppUser();
        appUser2.setExternalId("ext-id-232");
        appUserRepository.save(appUser1);
        appUserRepository.save(appUser2);
    }

    @Test
    public void findByDomainAndExternalId(){
        initializeAppUser();
        AppUser user = appUserRepository.findByDomainIdAndExternalId("test", "ext-id").get();
        assertThat(user, notNullValue());
    }

    @Test
    public void testQueriesWithBasicQuery(){
        AppUser user = createAppUser();
        user.set("color", "red");
        appUserRepository.save(user);
        MongoQueryConverter converter = new MongoQueryConverter(){
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
    public void queryUsers(){
        AppUser user = createAppUser();
        user.set("color", "red");
        appUserRepository.save(user);
        PageRequest request = PageRequest.of(0, 10);
        Page<AppUser> appUsers = appUserRepository.queryForUsers("color equals 'red' " +
                "and username equals 'user' and 'domainId' equals 'test'", request);
        assertThat(appUsers.getContent().size(), equalTo(1));
        assertThat(appUsers.getTotalElements(), equalTo(1L));
    }
}