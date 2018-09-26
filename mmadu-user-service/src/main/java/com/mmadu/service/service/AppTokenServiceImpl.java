package com.mmadu.service.service;

import com.mmadu.service.entities.AppToken;
import com.mmadu.service.exceptions.TokenNotFoundException;
import com.mmadu.service.repositories.AppTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppTokenServiceImpl implements AppTokenService {
    private KeyCipher keyCipher;
    private AppTokenRepository appTokenRepository;
    private TokenGenerator tokenGenerator;

    @Autowired
    public void setAppTokenRepository(AppTokenRepository appTokenRepository) {
        this.appTokenRepository = appTokenRepository;
    }

    @Autowired
    public void setKeyCipher(KeyCipher keyCipher) {
        this.keyCipher = keyCipher;
    }

    @Autowired
    public void setTokenGenerator(TokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public AppToken generateToken() {
        AppToken token = new AppToken();
        token.setValue(keyCipher.encrypt(tokenGenerator.generateToken()));
        return appTokenRepository.save(token);
    }

    @Override
    public AppToken refreshToken(String tokenId) {
        AppToken token = appTokenRepository.findById(tokenId).orElseThrow(() -> new TokenNotFoundException());
        token.setValue(keyCipher.encrypt(tokenGenerator.generateToken()));
        return token;
    }

    @Override
    public AppToken getToken(String tokenId) {
        return appTokenRepository.findById(tokenId)
                .orElseThrow(() -> new TokenNotFoundException());
    }

    @Override
    public boolean tokenMatches(String tokenId, String tokenValue) {
        AppToken token = appTokenRepository.findById(tokenId).orElseThrow(() -> new TokenNotFoundException());
        String clearToken = keyCipher.decrypt(token.getValue());
        return clearToken.equals(tokenValue);
    }
}
