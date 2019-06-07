package com.mmadu.service.services;

import com.mmadu.service.entities.AppUser;
import com.mmadu.service.exceptions.DomainNotFoundException;
import com.mmadu.service.exceptions.DuplicationException;
import com.mmadu.service.exceptions.UserNotFoundException;
import com.mmadu.service.model.UserView;
import com.mmadu.service.providers.UniqueUserIdGenerator;
import com.mmadu.service.repositories.AppDomainRepository;
import com.mmadu.service.repositories.AppUserRepository;
import com.sun.java.browser.plugin2.DOM;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserManagementServiceImpl.class)
public class UserManagementServiceImplTest {
    public static final String TEST_ID = "test-id";
    public static final String UNIQUE_USER_ID = "13234434";
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    private static final String DOMAIN_ID = "1234";

    @MockBean
    private AppUserRepository appUserRepository;
    @MockBean
    private AppDomainRepository appDomainRepository;
    @MockBean
    private UniqueUserIdGenerator uniqueUserIdGenerator;

    @Mock
    private Pageable pageable;

    @Autowired
    private UserManagementService userManagementService;
    @Captor
    private ArgumentCaptor<AppUser> appUserArgumentCaptor;

    @Before
    public void setUp() {
        doReturn(true).when(appDomainRepository).existsById(DOMAIN_ID);
        doReturn(UNIQUE_USER_ID).when(uniqueUserIdGenerator).generateUniqueId(DOMAIN_ID);
    }

    @Test
    public void createUser() {
        UserView userView = createUserView();
        userManagementService.createUser(DOMAIN_ID, userView);
        verify(appUserRepository, times(1)).save(appUserArgumentCaptor.capture());
        AppUser appUser = appUserArgumentCaptor.getValue();
        collector.checkThat(appUser.getDomainId(), equalTo(DOMAIN_ID));
        collector.checkThat(appUser.getPassword(), equalTo(userView.getPassword()));
        collector.checkThat(appUser.getUsername(), equalTo(userView.getUsername()));
        collector.checkThat(appUser.getAuthorities(), equalTo(userView.getAuthorities()));
        collector.checkThat(appUser.getRoles(), equalTo(userView.getRoles()));
        collector.checkThat(appUser.getProperties(), equalTo(userView.getProperties()));
        collector.checkThat(appUser.getExternalId(), equalTo(UNIQUE_USER_ID));
    }

    private UserView createUserView() {
        UserView userView = new UserView();
        userView.setUsername("user");
        userView.setPassword("password");
        userView.setRoles(asList("admin"));
        userView.setAuthorities(asList("manage-users"));
        userView.setProperty("color", "red");
        return userView;
    }

    @Test
    public void givenUserNameAlreadyExistsWhenCreateUserThrowUserExistsException() {
        expectedException.expectMessage("user already exists");
        expectedException.expect(DuplicationException.class);
        UserView userView = createUserView();
        doReturn(true).when(appUserRepository)
                .existsByUsernameAndDomainId(userView.getUsername(), DOMAIN_ID);
        userManagementService.createUser(DOMAIN_ID, userView);
    }

    @Test
    public void givenUserExternalIdAlreadyExistsWhenCreateUserThrowUserExistsException() {
        expectedException.expectMessage("user already exists");
        expectedException.expect(DuplicationException.class);
        UserView userView = createUserView();
        doReturn(true).when(appUserRepository)
                .existsByExternalIdAndDomainId(userView.getId(), DOMAIN_ID);
        userManagementService.createUser(DOMAIN_ID, userView);
    }

    @Test
    public void givenNullUsernameWhenCreateUserShouldThrowIllegalArgumentException() {
        expectedException.expectMessage("user missing username");
        expectedException.expect(IllegalArgumentException.class);
        UserView userView = createUserView();
        userView.setUsername(null);
        userManagementService.createUser(DOMAIN_ID, userView);
    }

    @Test
    public void givenBlankUsernameWhenCreateUserShouldThrowIllegalArgumentException() {
        expectedException.expectMessage("user missing username");
        expectedException.expect(IllegalArgumentException.class);
        UserView userView = createUserView();
        userView.setUsername("");
        userManagementService.createUser(DOMAIN_ID, userView);
    }

    @Test
    public void givenNullUserWhenCreateUserShouldThrowIllegalArgumentException() {
        expectedException.expectMessage("user cannot be null");
        expectedException.expect(IllegalArgumentException.class);
        userManagementService.createUser(DOMAIN_ID, null);
    }

    @Test
    public void givenDomainIDNotFoundShouldThrowDomainNotFoundException() {
        expectedException.expect(DomainNotFoundException.class);
        doReturn(false).when(appDomainRepository).existsById(DOMAIN_ID);
        userManagementService.createUser(DOMAIN_ID, createUserView());
    }

    @Test
    public void givenUserWithIdWhenCreateUserThenExternalIdShouldBeTheSame() {
        UserView userView = createUserView();
        userView.setId(TEST_ID);
        userManagementService.createUser(DOMAIN_ID, userView);
        verify(appUserRepository, times(1)).save(appUserArgumentCaptor.capture());
        AppUser appUser = appUserArgumentCaptor.getValue();
        collector.checkThat(appUser.getDomainId(), equalTo(DOMAIN_ID));
        collector.checkThat(appUser.getPassword(), equalTo(userView.getPassword()));
        collector.checkThat(appUser.getUsername(), equalTo(userView.getUsername()));
        collector.checkThat(appUser.getAuthorities(), equalTo(userView.getAuthorities()));
        collector.checkThat(appUser.getRoles(), equalTo(userView.getRoles()));
        collector.checkThat(appUser.getProperties(), equalTo(userView.getProperties()));
        collector.checkThat(appUser.getExternalId(), equalTo(TEST_ID));
    }

    @Test
    public void givenPagedListWhenGetAllUsersThenReturnPagedList(){
        AppUser user = new AppUser(DOMAIN_ID, createUserView());
        user.setId("id");
        PageImpl<AppUser> appUserPage = new PageImpl<>(asList(user));
        doReturn(appUserPage).when(appUserRepository).findByDomainId(DOMAIN_ID, pageable);
        Page<UserView> userViews = userManagementService.getAllUsers(DOMAIN_ID, pageable);
        assertThat(userViews.getContent().size(), equalTo(appUserPage.getContent().size()));
        collector.checkThat(userViews.getTotalElements(), equalTo(appUserPage.getTotalElements()));
        collector.checkThat(userViews.getTotalPages(), equalTo(appUserPage.getTotalPages()));
        collector.checkThat(userViews.getContent().get(0).getId(), equalTo(appUserPage.getContent().get(0).getExternalId()));
    }

    @Test
    public void givenUserNotFoundWhenGetUserByDomainAndExternalIdThenThrowUserNotFoundException() throws Exception {
        expectedException.expect(UserNotFoundException.class);
        doReturn(Optional.empty()).when(appUserRepository).findByDomainIdAndExternalId(DOMAIN_ID, UNIQUE_USER_ID);
        userManagementService.getUserByDomainIdAndExternalId(DOMAIN_ID, UNIQUE_USER_ID);
    }

    @Test
    public void givenDomainNotFoundWhenGetUserByDomainAndExternalIdThenThrowDomainNotFoundException() throws Exception {
        expectedException.expect(DomainNotFoundException.class);
        doReturn(false).when(appDomainRepository).existsById(DOMAIN_ID);
        userManagementService.getUserByDomainIdAndExternalId(DOMAIN_ID, UNIQUE_USER_ID);
    }

    @Test
    public void givenUserWhenGetUserByDomainAndExternalIdThenReturnUser(){
        AppUser user = new AppUser(DOMAIN_ID, createUserView());
        doReturn(Optional.of(user)).when(appUserRepository).findByDomainIdAndExternalId(DOMAIN_ID, UNIQUE_USER_ID);
        UserView userView = userManagementService.getUserByDomainIdAndExternalId(DOMAIN_ID, UNIQUE_USER_ID);
        assertThat(userView.getId(), equalTo(user.getExternalId()));
    }
}