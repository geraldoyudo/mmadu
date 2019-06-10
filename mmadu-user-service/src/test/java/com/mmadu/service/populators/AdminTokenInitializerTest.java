package com.mmadu.service.populators;

import static com.mmadu.service.utilities.DomainAuthenticationConstants.ADMIN_TOKEN_ID;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mmadu.service.providers.AppTokenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AdminTokenInitializerTest {

    @Mock
    private AppTokenService appTokenService;

    @InjectMocks
    private AdminTokenInitializer adminTokenInitializer = new AdminTokenInitializer();

    @Test
    public void initializeAdminToken() {
        adminTokenInitializer.initializeAdminToken();

        verify(appTokenService, times(1)).generateToken(ADMIN_TOKEN_ID);
    }
}