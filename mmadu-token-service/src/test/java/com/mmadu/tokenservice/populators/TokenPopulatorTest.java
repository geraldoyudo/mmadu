package com.mmadu.tokenservice.populators;

import com.mmadu.tokenservice.config.TokenConfigurationList;
import com.mmadu.tokenservice.entities.AppToken;
import com.mmadu.tokenservice.repositories.AppTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;

@DataMongoTest
@Import({
        TokenPopulator.class
})
public class TokenPopulatorTest {
    private static final String TOKEN_ID = "1234";
    private static final String TOKEN_VALUE = "1234232323";

    @Autowired
    private AppTokenRepository appTokenRepository;
    @MockBean
    private TokenConfigurationList tokenConfigurationList;
    @Autowired
    private TokenPopulator tokenPopulator;

    @BeforeEach
    void setUp() throws Exception {
        appTokenRepository.deleteAll();
        doReturn(tokens()).when(tokenConfigurationList).getTokens();
    }

    private static List<AppToken> tokens() {
        AppToken token = new AppToken();
        token.setId(TOKEN_ID);
        token.setValue(TOKEN_VALUE);
        return Arrays.asList(token);
    }

    @Test
    void populateTokens() throws Exception {
        tokenPopulator.setUpTokens();
        assertThat(appTokenRepository.count(), equalTo(1L));
        Optional<AppToken> token = appTokenRepository.findById(TOKEN_ID);
        assertThat(token.isPresent(), equalTo(true));
        assertAll(
                () -> assertThat(token.get().getValue(), is(equalTo(TOKEN_VALUE))),
                () -> assertThat(token.get().getId(), is(equalTo(TOKEN_ID)))
        );
    }
}