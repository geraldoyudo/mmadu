package com.mmadu.tokenservice.populators;

import com.mmadu.tokenservice.services.AppTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.mmadu.tokenservice.utilities.DomainAuthenticationConstants.ADMIN_TOKEN_ID;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdminTokenInitializerTest {

    @Mock
    private AppTokenService appTokenService;

    @InjectMocks
    private AdminTokenInitializer adminTokenInitializer = new AdminTokenInitializer();

    @Test
    void initializeAdminToken() {
        adminTokenInitializer.initializeAdminToken();

        verify(appTokenService, times(1)).generateToken(ADMIN_TOKEN_ID);
    }
}