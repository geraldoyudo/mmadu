package com.mmadu.service.services;

import com.mmadu.service.entities.AppUser;
import com.mmadu.service.exceptions.DomainNotFoundException;
import com.mmadu.service.exceptions.DuplicationException;
import com.mmadu.service.exceptions.UserNotFoundException;
import com.mmadu.service.model.PatchOperation;
import com.mmadu.service.model.UpdateRequest;
import com.mmadu.service.model.UserPatch;
import com.mmadu.service.model.UserView;
import com.mmadu.service.providers.UniqueUserIdGenerator;
import com.mmadu.service.repositories.AppDomainRepository;
import com.mmadu.service.repositories.AppUserRepository;
import org.assertj.core.util.Maps;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserManagementServiceImpl.class)
public class UserManagementServiceImplTest {
    public static final String TEST_ID = "test-id";
    public static final String UNIQUE_USER_ID = "13234434";
    public static final String APP_USER_ID = "2323";
    public static final String USERNAME = "user";
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
        userView.setUsername(USERNAME);
        userView.setPassword("password");
        userView.setRoles(asList("admin"));
        userView.setAuthorities(asList("manage-users"));
        userView.setProperty("color", "red");
        userView.setId(UNIQUE_USER_ID);
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
    public void givenPagedListWhenGetAllUsersThenReturnPagedList() {
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
    public void givenUserWhenGetUserByDomainAndExternalIdThenReturnUser() {
        AppUser user = new AppUser(DOMAIN_ID, createUserView());
        doReturn(Optional.of(user)).when(appUserRepository).findByDomainIdAndExternalId(DOMAIN_ID, UNIQUE_USER_ID);
        UserView userView = userManagementService.getUserByDomainIdAndExternalId(DOMAIN_ID, UNIQUE_USER_ID);
        assertThat(userView.getId(), equalTo(user.getExternalId()));
    }

    @Test
    public void givenUserNotFoundWhenGetUserByDomainAndUsernameThenThrowUserNotFoundException() throws Exception {
        expectedException.expect(UserNotFoundException.class);
        doReturn(Optional.empty()).when(appUserRepository).findByUsernameAndDomainId(USERNAME, DOMAIN_ID);
        userManagementService.getUserByDomainIdAndUsername(DOMAIN_ID, USERNAME);
    }

    @Test
    public void givenDomainNotFoundWhenGetUserByDomainAndUsernameThenThrowDomainNotFoundException() throws Exception {
        expectedException.expect(DomainNotFoundException.class);
        doReturn(false).when(appDomainRepository).existsById(DOMAIN_ID);
        userManagementService.getUserByDomainIdAndUsername(DOMAIN_ID, USERNAME);
    }

    @Test
    public void givenUserWhenGetUserByDomainAndUsernameThenReturnUser() {
        AppUser user = new AppUser(DOMAIN_ID, createUserView());
        doReturn(Optional.of(user)).when(appUserRepository).findByUsernameAndDomainId(USERNAME, DOMAIN_ID);
        UserView userView = userManagementService.getUserByDomainIdAndUsername(DOMAIN_ID, USERNAME);
        assertThat(userView.getUsername(), equalTo(user.getUsername()));
    }

    @Test
    public void givenUserNotFoundWhenDeleteUserByDomainAndExternalIdThenThrowUserNotFoundException() throws Exception {
        expectedException.expect(UserNotFoundException.class);
        doReturn(false).when(appUserRepository).existsByExternalIdAndDomainId(UNIQUE_USER_ID, DOMAIN_ID);
        userManagementService.deleteUserByDomainAndExternalId(DOMAIN_ID, UNIQUE_USER_ID);
    }

    @Test
    public void givenDomainNotFoundWhenDeleteUserByDomainAndExternalIdThenThrowDomainNotFoundException() throws Exception {
        expectedException.expect(DomainNotFoundException.class);
        doReturn(false).when(appDomainRepository).existsById(DOMAIN_ID);
        userManagementService.deleteUserByDomainAndExternalId(DOMAIN_ID, UNIQUE_USER_ID);
    }

    @Test
    public void givenUserWhenDeleteUserByDomainAndExternalIdThenReturnUser() {
        doReturn(true).when(appDomainRepository).existsById(DOMAIN_ID);
        doReturn(true).when(appUserRepository).existsByExternalIdAndDomainId(UNIQUE_USER_ID, DOMAIN_ID);
        userManagementService.deleteUserByDomainAndExternalId(DOMAIN_ID, UNIQUE_USER_ID);
        verify(appUserRepository, times(1))
                .deleteByDomainIdAndExternalId(DOMAIN_ID, UNIQUE_USER_ID);
    }

    @Test
    public void givenNullUserWhenUpdateUserThenThrowIllegalArgumentException() {
        expectedException.expectMessage("user cannot be null");
        expectedException.expect(IllegalArgumentException.class);
        userManagementService.updateUser(DOMAIN_ID, UNIQUE_USER_ID, null);
    }

    @Test
    public void givenNoDomainWhenUpdateUserThenThrowDomainNotFoundException() {
        expectedException.expect(DomainNotFoundException.class);
        doReturn(false).when(appDomainRepository).existsById(DOMAIN_ID);
        userManagementService.updateUser(DOMAIN_ID, UNIQUE_USER_ID, new UserView());
    }

    @Test
    public void givenNoUserWhenUpdateUserThenThrowUserNotFoundException() {
        expectedException.expect(UserNotFoundException.class);
        doReturn(Optional.empty()).when(appUserRepository).findByDomainIdAndExternalId(DOMAIN_ID, UNIQUE_USER_ID);
        userManagementService.updateUser(DOMAIN_ID, UNIQUE_USER_ID, new UserView());
    }

    @Test
    public void givenUserWhenUpdateUserThenSaveUpdatedUser() {
        doReturn(true).when(appDomainRepository).existsById(DOMAIN_ID);
        UserView userView = createUserView();
        AppUser appUser = new AppUser(DOMAIN_ID, userView);
        appUser.setId(APP_USER_ID);
        doReturn(Optional.of(appUser)).when(appUserRepository).findByDomainIdAndExternalId(DOMAIN_ID, UNIQUE_USER_ID);
        UserView modifiedUserView = new UserView(
                userView.getId() + "1",
                userView.getUsername() + "1",
                userView.getPassword() + "1",
                asList("member"),
                asList("sign-card"),
                Maps.newHashMap("color", "white")
        );
        userManagementService.updateUser(DOMAIN_ID, UNIQUE_USER_ID, modifiedUserView);
        verify(appUserRepository, times(1)).save(appUserArgumentCaptor.capture());
        AppUser updatedAppUser = appUserArgumentCaptor.getValue();
        collector.checkThat(updatedAppUser.getId(), equalTo(APP_USER_ID));
        collector.checkThat(updatedAppUser.getExternalId(), equalTo(UNIQUE_USER_ID + "1"));
        collector.checkThat(updatedAppUser.getPassword(), equalTo(userView.getPassword() + "1"));
        collector.checkThat(updatedAppUser.getUsername(), equalTo(userView.getUsername() + "1"));
        collector.checkThat(updatedAppUser.getRoles(), equalTo(asList("member")));
        collector.checkThat(updatedAppUser.getAuthorities(), equalTo(asList("sign-card")));
        collector.checkThat(updatedAppUser.getProperties(), equalTo(Maps.newHashMap("color", "white")));
    }

    @Test
    public void givenQueryAndDomainIdWhenQueryUsersShouldAddDomainIdToQuery() {
        String query = "nationality equals 'nigerian'";
        Page<AppUser> userPage = new PageImpl<>(Collections.emptyList());
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        doReturn(userPage).when(appUserRepository).queryForUsers(anyString(), any(Pageable.class));
        userManagementService.queryUsers(DOMAIN_ID, query, PageRequest.of(0, 10));
        verify(appUserRepository, times(1))
                .queryForUsers(queryCaptor.capture(), any(Pageable.class));
        assertThat(queryCaptor.getValue(), equalTo(query + " and (domainId equals '1234')"));
    }

    @Test
    public void givenQueryWithIdAndDomainIdWhenQueryUsersShouldTranslateIdToExternalId() {
        String query = "nationality equals 'id-nigerian' and id equals '13234434'";
        Page<AppUser> userPage = new PageImpl<>(Collections.emptyList());
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        doReturn(userPage).when(appUserRepository).queryForUsers(anyString(), any(Pageable.class));
        userManagementService.queryUsers(DOMAIN_ID, query, PageRequest.of(0, 10));
        verify(appUserRepository, times(1))
                .queryForUsers(queryCaptor.capture(), any(Pageable.class));
        assertThat(queryCaptor.getValue(), equalTo("nationality equals 'id-nigerian' " +
                "and externalId equals '13234434' " +
                "and (domainId equals '1234')"));
    }

    @Test
    public void givenEmptyQueryAndDomainIdWhenQueryUsersShouldQueryAllInDomain() {
        String query = "";
        Page<AppUser> userPage = new PageImpl<>(Collections.emptyList());
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        doReturn(userPage).when(appUserRepository).queryForUsers(anyString(), any(Pageable.class));
        userManagementService.queryUsers(DOMAIN_ID, query, PageRequest.of(0, 10));
        verify(appUserRepository, times(1))
                .queryForUsers(queryCaptor.capture(), any(Pageable.class));
        assertThat(queryCaptor.getValue(), equalTo("(domainId equals '1234')"));
    }

    @Test
    public void givenNullQueryAndDomainIdWhenQueryUsersShouldQueryAllInDomain() {
        Page<AppUser> userPage = new PageImpl<>(Collections.emptyList());
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        doReturn(userPage).when(appUserRepository).queryForUsers(anyString(), any(Pageable.class));
        userManagementService.queryUsers(DOMAIN_ID, null, PageRequest.of(0, 10));
        verify(appUserRepository, times(1))
                .queryForUsers(queryCaptor.capture(), any(Pageable.class));
        assertThat(queryCaptor.getValue(), equalTo("(domainId equals '1234')"));
    }

    @Test
    public void givenQueryAndDomainIdAndUpdateRequestWhenQueryUsersShouldAddDomainIdToQuery() {
        String query = "nationality equals 'nigerian'";
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        userManagementService.patchUpdateUsers(DOMAIN_ID, query, testUpdateRequest());
        verify(appUserRepository, times(1))
                .updateUsers(queryCaptor.capture(), any(UpdateRequest.class));
        assertThat(queryCaptor.getValue(), equalTo(query + " and (domainId equals '1234')"));
    }

    @Test
    public void givenQueryWithIdAndDomainIdAndUpdateRequestWhenQueryUsersShouldTranslateIdToExternalId() {
        String query = "nationality equals 'id-nigerian' and id equals '13234434'";
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        userManagementService.patchUpdateUsers(DOMAIN_ID, query, testUpdateRequest());
        verify(appUserRepository, times(1))
                .updateUsers(queryCaptor.capture(), any(UpdateRequest.class));
        assertThat(queryCaptor.getValue(), equalTo("nationality equals 'id-nigerian' " +
                "and externalId equals '13234434' " +
                "and (domainId equals '1234')"));
    }

    @Test
    public void givenEmptyQueryAndDomainIdAndUpdateRequestWhenQueryUsersShouldUpdateAllInDomain() {
        String query = "";
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        userManagementService.patchUpdateUsers(DOMAIN_ID, query, testUpdateRequest());
        verify(appUserRepository, times(1))
                .updateUsers(queryCaptor.capture(), any(UpdateRequest.class));
        assertThat(queryCaptor.getValue(), equalTo("(domainId equals '1234')"));
    }

    @Test
    public void givenNullQueryAndDomainIdAndUpdateRequestWhenQueryUsersShouldQueryAllInDomain() {
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        userManagementService.patchUpdateUsers(DOMAIN_ID, null, testUpdateRequest());
        verify(appUserRepository, times(1))
                .updateUsers(queryCaptor.capture(), any(UpdateRequest.class));
        assertThat(queryCaptor.getValue(), equalTo("(domainId equals '1234')"));
    }

    private UpdateRequest testUpdateRequest() {
        UpdateRequest request = new UpdateRequest();
        request.addUpdate(new UserPatch(PatchOperation.SET, "property", "value"));
        return request;
    }

    @Test
    public void givenNullUpdateRequestWhenQueryUsersShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Update request cannot be null");
        userManagementService.patchUpdateUsers(DOMAIN_ID, "", null);
    }

    @Test
    public void givenEmptyUpdateRequestWhenQueryUsersShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("User patches cannot be empty");
        userManagementService.patchUpdateUsers(DOMAIN_ID, "", new UpdateRequest());
    }
}