package com.mmadu.registration.services;

import com.mmadu.registration.entities.RegistrationProfile;
import com.mmadu.registration.exceptions.UserFormValidationException;
import com.mmadu.registration.models.UserForm;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static com.mmadu.registration.utils.EntityUtils.DOMAIN_ID;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = RegistrationServiceImpl.class)
public class RegistrationServiceImplTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    @MockBean
    private RegistrationProfileService registrationProfileService;
    @Autowired
    private RegistrationService registrationService;
    @MockBean
    private MmaduUserServiceClient mmaduUserServiceClient;
    @Captor
    private ArgumentCaptor<Map<String, Object>> userCaptor;

    @Test
    public void givenUserNameAbsentWhenRegisterUserThenThrowUserFormValidationException() {
        UserForm userForm = new UserForm();
        userForm.set("password", "password");
        expectedException.expectMessage("username cannot be empty");
        expectedException.expect(UserFormValidationException.class);
        registrationService.registerUser(DOMAIN_ID, userForm);
    }

    @Test
    public void givenUserWhenRegisterUserThenAddDefaultRolesAndAuthorities() {
        UserForm userForm = new UserForm();
        userForm.set("username", "user");
        RegistrationProfile profile = getRegistrationProfile();
        doReturn(profile).when(registrationProfileService).getProfileForDomain(DOMAIN_ID);
        registrationService.registerUser(DOMAIN_ID, userForm);
        verify(mmaduUserServiceClient, times(1)).addUsers(eq(DOMAIN_ID), userCaptor.capture());
        Map<String, Object> userProperties = userCaptor.getValue();
        assertThat(userProperties.get("roles"), equalTo(profile.getDefaultRoles()));
        assertThat(userProperties.get("authorities"), equalTo(profile.getDefaultAuthorities()));
    }

    private RegistrationProfile getRegistrationProfile() {
        RegistrationProfile profile = new RegistrationProfile();
        profile.setDomainId(DOMAIN_ID);
        profile.setId("1");
        profile.setDefaultRedirectUrl("http://google.com");
        profile.setDefaultAuthorities(asList("manage-list"));
        profile.setDefaultRoles(asList("member"));
        return profile;
    }

    @Test
    public void givenNullPasswordWhenAddUserEnsurePasswordIsEmpty() {
        UserForm userForm = new UserForm();
        userForm.set("username", "user");
        RegistrationProfile profile = getRegistrationProfile();
        doReturn(profile).when(registrationProfileService).getProfileForDomain(DOMAIN_ID);
        registrationService.registerUser(DOMAIN_ID, userForm);
        verify(mmaduUserServiceClient, times(1)).addUsers(eq(DOMAIN_ID), userCaptor.capture());
        Map<String, Object> userProperties = userCaptor.getValue();
        assertThat(userProperties.get("password"), equalTo(""));
    }

    @Test
    public void givenNullUserFormThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("user form cannot be null");
        registrationService.registerUser(DOMAIN_ID, null);
    }
}