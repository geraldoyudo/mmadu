package com.mmadu.tokenservice.config;

import com.mmadu.tokenservice.entities.AppToken;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "mmadu.token-config")
public class TokenConfigurationList {

    private List<AppToken> tokens = new ArrayList<>();

    public List<AppToken> getTokens() {
        return tokens;
    }

    public void setTokens(List<AppToken> tokens) {
        this.tokens = tokens;
    }
}
