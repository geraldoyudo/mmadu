package com.mmadu.service.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.mmadu.service.entities.AppToken;
import com.mmadu.service.repositories.AppTokenRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import(AppTokenServiceImpl.class)
@DataMongoTest
public class AppTokenServiceImplTest {
    @Rule
    public final ErrorCollector collector = new ErrorCollector();

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

    @Before
    public void setUp(){
        appTokenRepository.deleteAll();
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0)).when(keyCipher).encrypt(anyString());
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0)).when(keyCipher).decrypt(anyString());
        when(tokenGenerator.generateToken()).thenReturn(TOKEN).thenReturn(TOKEN_2);
    }

    @Test
    public void generateToken() {
        AppToken token = appTokenService.generateToken();
        collector.checkThat(token, notNullValue());
        collector.checkThat(token.getValue(), equalTo(TOKEN));
        collector.checkThat(token.getId(), notNullValue());
    }

    @Test
    public void refreshToken() {
        AppToken token = appTokenService.generateToken();
        collector.checkThat(token.getValue(), equalTo(TOKEN));
        AppToken refreshedToken = appTokenService.resetToken(token.getId());
        assertThat(refreshedToken.getId(), equalTo(token.getId()));
        assertThat(refreshedToken.getValue(), equalTo(TOKEN_2));
    }

    @Test
    public void getToken() {
        AppToken token = appTokenService.generateToken();
        AppToken retrievedToken = appTokenService.getToken(token.getId());
        assertThat(retrievedToken.getValue(), equalTo(token.getValue()));
        assertThat(retrievedToken.getValue(), equalTo(TOKEN));
    }

    @Test
    public void tokenPositiveMatch() {
        AppToken token = appTokenService.generateToken();
        assertThat(appTokenService.tokenMatches(token.getId(), TOKEN), is(true));
    }

    @Test
    public void tokenNegativeMatch() {
        AppToken token = appTokenService.generateToken();
        assertThat(appTokenService.tokenMatches(token.getId(), "323237392323"), is(false));
    }
}