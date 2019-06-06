package com.mmadu.service.services;

import com.mmadu.service.entities.AppUser;
import com.mmadu.service.exceptions.DomainNotFoundException;
import com.mmadu.service.exceptions.DuplicationException;
import com.mmadu.service.model.UserView;
import com.mmadu.service.repositories.AppDomainRepository;
import com.mmadu.service.repositories.AppUserRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserManagementServiceImpl.class)
public class UserManagementServiceImplTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    private static final String DOMAIN_ID = "1234";

    @MockBean
    private AppUserRepository appUserRepository;
    @MockBean
    private AppDomainRepository appDomainRepository;

    @Autowired
    private UserManagementService userManagementService;
    @Captor
    private ArgumentCaptor<AppUser> appUserArgumentCaptor;

    @Before
    public void setUp() {
        doReturn(true).when(appDomainRepository).existsById(DOMAIN_ID);
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
    public void givenUserAlreadyExistsWhenCreateUserThrowUserExistsException() {
        expectedException.expectMessage("user already exists");
        expectedException.expect(DuplicationException.class);
        UserView userView = createUserView();
        doReturn(true).when(appUserRepository)
                .existsByUsernameAndDomainId(userView.getUsername(), DOMAIN_ID);
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
}