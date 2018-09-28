package com.mmadu.service.populators;

import com.mmadu.service.config.TokenConfigurationList;
import com.mmadu.service.entities.AppToken;
import com.mmadu.service.repositories.AppTokenRepository;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TokenPopulator {
    @Autowired
    private TokenConfigurationList tokenConfigurationList;
    @Autowired
    private AppTokenRepository appTokenRepository;

    @PostConstruct
    public void setUpTokens(){
        tokenConfigurationList.getTokens().forEach(this::saveToken);
    }

    private void saveToken(AppToken appToken) {
        String tokenId = appToken.getId();
        if(!StringUtils.isEmpty(tokenId) && appTokenRepository.existsById(appToken.getId()))
            return;
        appTokenRepository.save(appToken);
    }
}
