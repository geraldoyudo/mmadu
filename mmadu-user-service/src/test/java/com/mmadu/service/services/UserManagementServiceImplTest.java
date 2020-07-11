package com.mmadu.service.services;

import com.mmadu.service.entities.AppUser;
import com.mmadu.service.exceptions.DomainNotFoundException;
import com.mmadu.service.exceptions.DuplicationException;
import com.mmadu.service.exceptions.UserNotFoundException;
import com.mmadu.service.models.PatchOperation;
import com.mmadu.service.models.UpdateRequest;
import com.mmadu.service.models.UserPatch;
import com.mmadu.service.models.UserView;
import com.mmadu.service.providers.UniqueUserIdGenerator;
import com.mmadu.service.repositories.AppDomainRepository;
import com.mmadu.service.repositories.AppUserRepository;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UserManagementServiceImpl.class)
public class UserManagementServiceImplTest {
    public static final String TEST_ID = "test-id";
    public static final String UNIQUE_USER_ID = "13234434";
    public static final String APP_USER_ID = "2323";
    public static final String USERNAME = "user";

    private static final String DOMAIN_ID = "1234";

    @MockBean
    private AppUserRepository appUserRepository;
    @MockBean
    private AppDomainRepository appDomainRepository;
    @MockBean
    private UniqueUserIdGenerator uniqueUserIdGenerator;
    @MockBean
    private GroupService groupService;
    @MockBean
    private AuthorityManagementService authorityManagementService;
    @MockBean
    private RoleManagementService roleManagementService;
    @Mock
    private Pageable pageable;

    @Autowired
    private UserManagementService userManagementService;
    @Captor
    private ArgumentCaptor<AppUser> appUserArgumentCaptor;

    @BeforeEach
    void setUp() {
        doReturn(true).when(appDomainRepository).existsById(DOMAIN_ID);
        doReturn(UNIQUE_USER_ID).when(uniqueUserIdGenerator).generateUniqueId(DOMAIN_ID);
    }

    @Test
    void createUser() {
        UserView userView = createUserView();
        when(appUserRepository.save(appUserArgumentCaptor.capture())).thenAnswer(iom -> iom.getArgument(0));
        userManagementService.createUser(DOMAIN_ID, userView);
        AppUser appUser = appUserArgumentCaptor.getValue();
        assertAll(
                () -> assertThat(appUser.getDomainId(), equalTo(DOMAIN_ID)),
                () -> assertThat(appUser.getPassword(), equalTo(userView.getPassword())),
                () -> assertThat(appUser.getUsername(), equalTo(userView.getUsername())),
                () -> assertThat(appUser.getProperties(), equalTo(userView.getProperties())),
                () -> assertThat(appUser.getExternalId(), equalTo(UNIQUE_USER_ID)),
                () -> verify(appUserRepository, times(1)).save(any())
        );

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
    void givenUserNameAlreadyExistsWhenCreateUserThrowUserExistsException() {
        UserView userView = createUserView();
        doReturn(true).when(appUserRepository)
                .existsByUsernameAndDomainId(userView.getUsername(), DOMAIN_ID);
        ;
        Exception ex = assertThrows(DuplicationException.class, () -> userManagementService.createUser(DOMAIN_ID, userView));
        assertEquals("user already exists", ex.getMessage());
    }

    @Test
    void givenUserExternalIdAlreadyExistsWhenCreateUserThrowUserExistsException() {
        UserView userView = createUserView();
        doReturn(true).when(appUserRepository)
                .existsByExternalIdAndDomainId(userView.getId(), DOMAIN_ID);
        Exception ex = assertThrows(DuplicationException.class, () -> userManagementService.createUser(DOMAIN_ID, userView));
        assertEquals("user already exists", ex.getMessage());
    }

    @Test
    void givenNullUsernameWhenCreateUserShouldThrowIllegalArgumentException() {
        UserView userView = createUserView();
        userView.setUsername(null);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> userManagementService.createUser(DOMAIN_ID, userView));
        assertEquals("user missing username", ex.getMessage());
    }

    @Test
    void givenBlankUsernameWhenCreateUserShouldThrowIllegalArgumentException() {
        UserView userView = createUserView();
        userView.setUsername("");
        Exception ex = assertThrows(IllegalArgumentException.class, () -> userManagementService.createUser(DOMAIN_ID, userView));
        assertEquals("user missing username", ex.getMessage());
    }

    @Test
    void givenNullUserWhenCreateUserShouldThrowIllegalArgumentException() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> userManagementService.createUser(DOMAIN_ID, null));
        assertEquals("user cannot be null", ex.getMessage());
    }

    @Test
    void givenDomainIDNotFoundShouldThrowDomainNotFoundException() {
        doReturn(false).when(appDomainRepository).existsById(DOMAIN_ID);
        assertThrows(DomainNotFoundException.class, () -> userManagementService.createUser(DOMAIN_ID, createUserView()));
    }

    @Test
    void givenUserWithIdWhenCreateUserThenExternalIdShouldBeTheSame() {
        UserView userView = createUserView();
        userView.setId(TEST_ID);
        when(appUserRepository.save(appUserArgumentCaptor.capture())).thenAnswer(iom -> iom.getArgument(0));
        userManagementService.createUser(DOMAIN_ID, userView);
        AppUser appUser = appUserArgumentCaptor.getValue();
        assertAll(
                () -> assertThat(appUser.getDomainId(), equalTo(DOMAIN_ID)),
                () -> assertThat(appUser.getPassword(), equalTo(userView.getPassword())),
                () -> assertThat(appUser.getUsername(), equalTo(userView.getUsername())),
                () -> assertThat(appUser.getProperties(), equalTo(userView.getProperties())),
                () -> assertThat(appUser.getExternalId(), equalTo(TEST_ID)),
                () -> verify(appUserRepository, times(1)).save(any())
        );
    }

    @Test
    void givenPagedListWhenGetAllUsersThenReturnPagedList() {
        AppUser user = new AppUser(DOMAIN_ID, createUserView());
        user.setId("id");
        PageImpl<AppUser> appUserPage = new PageImpl<>(asList(user));
        doReturn(appUserPage).when(appUserRepository).findByDomainId(DOMAIN_ID, pageable);
        Page<UserView> userViews = userManagementService.getAllUsers(DOMAIN_ID, pageable);
        assertThat(userViews.getContent().size(), equalTo(appUserPage.getContent().size()));
        assertAll(
                () -> assertThat(userViews.getTotalElements(), equalTo(appUserPage.getTotalElements())),
                () -> assertThat(userViews.getTotalPages(), equalTo(appUserPage.getTotalPages())),
                () -> assertThat(userViews.getContent().get(0).getId(), equalTo(appUserPage.getContent().get(0).getExternalId()))
        );
    }

    @Test
    void givenUserNotFoundWhenGetUserByDomainAndExternalIdThenThrowUserNotFoundException() throws Exception {
        doReturn(Optional.empty()).when(appUserRepository).findByDomainIdAndExternalId(DOMAIN_ID, UNIQUE_USER_ID);
        assertThrows(UserNotFoundException.class, () -> userManagementService.getUserByDomainIdAndExternalId(DOMAIN_ID, UNIQUE_USER_ID));
    }

    @Test
    void givenDomainNotFoundWhenGetUserByDomainAndExternalIdThenThrowDomainNotFoundException() throws Exception {
        doReturn(false).when(appDomainRepository).existsById(DOMAIN_ID);
        assertThrows(DomainNotFoundException.class, () -> userManagementService.getUserByDomainIdAndExternalId(DOMAIN_ID, UNIQUE_USER_ID));
    }

    @Test
    void givenUserWhenGetUserByDomainAndExternalIdThenReturnUser() {
        AppUser user = new AppUser(DOMAIN_ID, createUserView());
        doReturn(Optional.of(user)).when(appUserRepository).findByDomainIdAndExternalId(DOMAIN_ID, UNIQUE_USER_ID);
        UserView userView = userManagementService.getUserByDomainIdAndExternalId(DOMAIN_ID, UNIQUE_USER_ID);
        assertThat(userView.getId(), equalTo(user.getExternalId()));
    }

    @Test
    void givenUserNotFoundWhenGetUserByDomainAndUsernameThenThrowUserNotFoundException() throws Exception {
        doReturn(Optional.empty()).when(appUserRepository).findByUsernameAndDomainId(USERNAME, DOMAIN_ID);
        assertThrows(UserNotFoundException.class, () -> userManagementService.getUserByDomainIdAndUsername(DOMAIN_ID, USERNAME));
    }

    @Test
    void givenDomainNotFoundWhenGetUserByDomainAndUsernameThenThrowDomainNotFoundException() throws Exception {
        doReturn(false).when(appDomainRepository).existsById(DOMAIN_ID);
        assertThrows(DomainNotFoundException.class, () -> userManagementService.getUserByDomainIdAndUsername(DOMAIN_ID, USERNAME));
    }

    @Test
    void givenUserWhenGetUserByDomainAndUsernameThenReturnUser() {
        AppUser user = new AppUser(DOMAIN_ID, createUserView());
        doReturn(Optional.of(user)).when(appUserRepository).findByUsernameAndDomainId(USERNAME, DOMAIN_ID);
        UserView userView = userManagementService.getUserByDomainIdAndUsername(DOMAIN_ID, USERNAME);
        assertThat(userView.getUsername(), equalTo(user.getUsername()));
    }

    @Test
    void givenUserNotFoundWhenDeleteUserByDomainAndExternalIdThenThrowUserNotFoundException() throws Exception {
        doReturn(false).when(appUserRepository).existsByExternalIdAndDomainId(UNIQUE_USER_ID, DOMAIN_ID);
        assertThrows(UserNotFoundException.class, () -> userManagementService.deleteUserByDomainAndExternalId(DOMAIN_ID, UNIQUE_USER_ID));
    }

    @Test
    void givenDomainNotFoundWhenDeleteUserByDomainAndExternalIdThenThrowDomainNotFoundException() throws Exception {
        doReturn(false).when(appDomainRepository).existsById(DOMAIN_ID);
        assertThrows(DomainNotFoundException.class, () -> userManagementService.deleteUserByDomainAndExternalId(DOMAIN_ID, UNIQUE_USER_ID));
    }

    @Test
    void givenUserWhenDeleteUserByDomainAndExternalIdThenReturnUser() {
        doReturn(true).when(appDomainRepository).existsById(DOMAIN_ID);
        doReturn(true).when(appUserRepository).existsByExternalIdAndDomainId(UNIQUE_USER_ID, DOMAIN_ID);
        userManagementService.deleteUserByDomainAndExternalId(DOMAIN_ID, UNIQUE_USER_ID);
        verify(appUserRepository, times(1))
                .deleteByDomainIdAndExternalId(DOMAIN_ID, UNIQUE_USER_ID);
    }

    @Test
    void givenNullUserWhenUpdateUserThenThrowIllegalArgumentException() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> userManagementService.updateUser(DOMAIN_ID, UNIQUE_USER_ID, null));
        assertEquals("user cannot be null", ex.getMessage());
    }

    @Test
    void givenNoDomainWhenUpdateUserThenThrowDomainNotFoundException() {
        doReturn(false).when(appDomainRepository).existsById(DOMAIN_ID);
        assertThrows(DomainNotFoundException.class, () -> userManagementService.updateUser(DOMAIN_ID, UNIQUE_USER_ID, new UserView()));
    }

    @Test
    void givenNoUserWhenUpdateUserThenThrowUserNotFoundException() {
        doReturn(Optional.empty()).when(appUserRepository).findByDomainIdAndExternalId(DOMAIN_ID, UNIQUE_USER_ID);
        assertThrows(UserNotFoundException.class, () -> userManagementService.updateUser(DOMAIN_ID, UNIQUE_USER_ID, new UserView()));
    }

    @Test
    void givenUserWhenUpdateUserThenSaveUpdatedUser() {
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
        assertAll(
                () -> assertThat(updatedAppUser.getId(), equalTo(APP_USER_ID)),
                () -> assertThat(updatedAppUser.getExternalId(), equalTo(UNIQUE_USER_ID + "1")),
                () -> assertThat(updatedAppUser.getPassword(), equalTo(userView.getPassword() + "1")),
                () -> assertThat(updatedAppUser.getUsername(), equalTo(userView.getUsername() + "1")),
                () -> assertThat(updatedAppUser.getProperties(), equalTo(Maps.newHashMap("color", "white")))
        );
    }

    @Test
    void givenQueryAndDomainIdWhenQueryUsersShouldAddDomainIdToQuery() {
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
    void givenQueryWithIdAndDomainIdWhenQueryUsersShouldTranslateIdToExternalId() {
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
    void givenEmptyQueryAndDomainIdWhenQueryUsersShouldQueryAllInDomain() {
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
    void givenNullQueryAndDomainIdWhenQueryUsersShouldQueryAllInDomain() {
        Page<AppUser> userPage = new PageImpl<>(Collections.emptyList());
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        doReturn(userPage).when(appUserRepository).queryForUsers(anyString(), any(Pageable.class));
        userManagementService.queryUsers(DOMAIN_ID, null, PageRequest.of(0, 10));
        verify(appUserRepository, times(1))
                .queryForUsers(queryCaptor.capture(), any(Pageable.class));
        assertThat(queryCaptor.getValue(), equalTo("(domainId equals '1234')"));
    }

    @Test
    void givenQueryAndDomainIdAndUpdateRequestWhenQueryUsersShouldAddDomainIdToQuery() {
        String query = "nationality equals 'nigerian'";
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        userManagementService.patchUpdateUsers(DOMAIN_ID, query, testUpdateRequest());
        verify(appUserRepository, times(1))
                .updateUsers(queryCaptor.capture(), any(UpdateRequest.class));
        assertThat(queryCaptor.getValue(), equalTo(query + " and (domainId equals '1234')"));
    }

    @Test
    void givenQueryWithIdAndDomainIdAndUpdateRequestWhenQueryUsersShouldTranslateIdToExternalId() {
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
    void givenEmptyQueryAndDomainIdAndUpdateRequestWhenQueryUsersShouldUpdateAllInDomain() {
        String query = "";
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        userManagementService.patchUpdateUsers(DOMAIN_ID, query, testUpdateRequest());
        verify(appUserRepository, times(1))
                .updateUsers(queryCaptor.capture(), any(UpdateRequest.class));
        assertThat(queryCaptor.getValue(), equalTo("(domainId equals '1234')"));
    }

    @Test
    void givenNullQueryAndDomainIdAndUpdateRequestWhenQueryUsersShouldQueryAllInDomain() {
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
    void givenNullUpdateRequestWhenQueryUsersShouldThrowIllegalArgumentException() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> userManagementService.patchUpdateUsers(DOMAIN_ID, "", null));
        assertEquals("Update request cannot be null", ex.getMessage());
    }

    @Test
    void givenEmptyUpdateRequestWhenQueryUsersShouldThrowIllegalArgumentException() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> userManagementService.patchUpdateUsers(DOMAIN_ID, "", new UpdateRequest()));
        assertEquals("User patches cannot be empty", ex.getMessage());
    }
}