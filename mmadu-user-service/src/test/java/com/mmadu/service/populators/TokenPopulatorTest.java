package com.mmadu.service.populators;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;

import com.mmadu.service.config.TokenConfigurationList;
import com.mmadu.service.entities.AppToken;
import com.mmadu.service.repositories.AppTokenRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@DataMongoTest
@RunWith(SpringRunner.class)
@Import(TokenPopulator.class)
public class TokenPopulatorTest {
    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    private static final String TOKEN_ID = "1234";
    private static final String TOKEN_VALUE = "1234232323";

    @Autowired
    private AppTokenRepository appTokenRepository;
    @MockBean
    private TokenConfigurationList tokenConfigurationList;
    @Autowired
    private TokenPopulator tokenPopulator;

    @Before
    public void setUp() throws Exception {
        appTokenRepository.deleteAll();
        doReturn(tokens()).when(tokenConfigurationList).getTokens();
    }

    private static List<AppToken> tokens(){
        AppToken token = new AppToken();
        token.setId(TOKEN_ID);
        token.setValue(TOKEN_VALUE);
        return Arrays.asList(token);
    }

    @Test
    public void populateTokens() throws Exception {
        tokenPopulator.setUpTokens();
        assertThat(appTokenRepository.count(), equalTo(1L));
        Optional<AppToken> token = appTokenRepository.findById(TOKEN_ID);
        assertThat(token.isPresent(), equalTo(true));
        collector.checkThat(token.get().getValue(), is(equalTo(TOKEN_VALUE)));
        collector.checkThat(token.get().getId(), is(equalTo(TOKEN_ID)));
    }
}