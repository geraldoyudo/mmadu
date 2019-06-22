package com.mmadu.tokenservice.populators;

import com.mmadu.tokenservice.config.TokenConfigurationList;
import com.mmadu.tokenservice.entities.AppToken;
import com.mmadu.tokenservice.repositories.AppTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@Component
public class TokenPopulator {
    @Autowired
    private TokenConfigurationList tokenConfigurationList;
    @Autowired
    private AppTokenRepository appTokenRepository;

    @PostConstruct
    public void setUpTokens() {
        tokenConfigurationList.getTokens().forEach(this::saveToken);
    }

    private void saveToken(AppToken appToken) {
        String tokenId = appToken.getId();
        if (!StringUtils.isEmpty(tokenId) && appTokenRepository.existsById(appToken.getId()))
            return;
        appTokenRepository.save(appToken);
    }
}
