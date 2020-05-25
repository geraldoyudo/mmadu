package com.mmadu.tokenservice.services;

import com.mmadu.encryption.KeyCipher;
import com.mmadu.tokenservice.entities.AppToken;
import com.mmadu.tokenservice.exceptions.TokenNotFoundException;
import com.mmadu.tokenservice.providers.TokenGenerator;
import com.mmadu.tokenservice.repositories.AppTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.codec.Hex;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Import({
        AppTokenServiceImpl.class
})
@DataMongoTest
public class AppTokenServiceImplTest {
    private static final String TOKEN = new String(Hex.encode(new byte[128]));
    private static final String TOKEN_2 = TOKEN.replace("0", "1");

    @MockBean
    private KeyCipher keyCipher;
    @Autowired
    private AppTokenRepository appTokenRepository;
    @MockBean
    private TokenGenerator tokenGenerator;

    @Autowired
    private AppTokenService appTokenService;

    @BeforeEach
    void setUp() {
        appTokenRepository.deleteAll();
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0)).when(keyCipher).encrypt(anyString());
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0)).when(keyCipher).decrypt(anyString());
        when(tokenGenerator.generateToken()).thenReturn(TOKEN).thenReturn(TOKEN_2);
    }

    @Test
    void generateToken() {
        AppToken token = appTokenService.generateToken();
        assertAll(
                () -> assertThat(token, notNullValue()),
                () -> assertThat(token.getValue(), equalTo(TOKEN)),
                () -> assertThat(token.getId(), notNullValue())
        );
    }

    @Test
    void refreshToken() {
        AppToken token = appTokenService.generateToken();
        AppToken refreshedToken = appTokenService.resetToken(token.getId());
        assertAll(
                () -> assertThat(token.getValue(), equalTo(TOKEN)),
                () -> assertThat(refreshedToken.getId(), equalTo(token.getId())),
                () -> assertThat(refreshedToken.getValue(), equalTo(TOKEN_2))
        );
    }

    @Test
    void getToken() {
        AppToken token = appTokenService.generateToken();
        AppToken retrievedToken = appTokenService.getToken(token.getId());
        assertAll(
                () -> assertThat(retrievedToken.getValue(), equalTo(token.getValue())),
                () -> assertThat(retrievedToken.getValue(), equalTo(TOKEN))
        );
    }

    @Test
    void tokenPositiveMatch() {
        AppToken token = appTokenService.generateToken();
        assertThat(appTokenService.tokenMatches(token.getId(), TOKEN), is(true));
    }

    @Test
    void tokenNegativeMatch() {
        AppToken token = appTokenService.generateToken();
        assertThat(appTokenService.tokenMatches(token.getId(), "323237392323"), is(false));
    }

    @Test
    void givenTokenIdExistsWhenGenerateTokenWithIdThenReturnSameToken() {
        AppTokenService spiedTokenService = spy(appTokenService);
        AppToken token = spiedTokenService.generateToken();
        doReturn(token).when(spiedTokenService).getToken(token.getId());
        AppToken generatedToken = spiedTokenService.generateToken(token.getId());
        assertThat(generatedToken, equalTo(token));
    }

    @Test
    void givenTokenIdNotExistsWhenGenerateTokenWithIdThenReturnNewToken() {
        final String tokenId = "token-id";
        AppTokenService spiedTokenService = spy(appTokenService);
        doThrow(new TokenNotFoundException()).when(spiedTokenService).getToken(tokenId);
        AppToken generatedToken = spiedTokenService.generateToken(tokenId);
        assertAll(
                () -> assertThat(generatedToken, notNullValue()),
                () -> assertThat(generatedToken.getId(), equalTo(tokenId)),
                () -> assertThat(generatedToken.getValue(), equalTo(TOKEN))
        );
    }

    @Test
    void givenTokenIdExistsAndBlankTokenValueWhenMatchThenReturnTrue() {
        when(tokenGenerator.generateToken()).thenReturn("");
        AppToken token = appTokenService.generateToken();
        assertThat(appTokenService.tokenMatches(token.getId(), "any-abritrary-value"), equalTo(true));
    }
}