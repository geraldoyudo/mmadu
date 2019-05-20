package com.mmadu.service.service;

import com.mmadu.service.entities.AppToken;
import com.mmadu.service.exceptions.TokenNotFoundException;

public interface AppTokenService {

    AppToken generateToken();

    AppToken generateTokenWithId(String tokenId);

    AppToken resetToken(String tokenId);

    AppToken getToken(String tokenId);

    boolean tokenMatches(String tokenId, String tokenValue);

    default AppToken generateToken(String tokenId){
        try {
            AppToken token = getToken(tokenId);
            return token;
        }catch (TokenNotFoundException ex){
            return generateTokenWithId(tokenId);
        }
    }
}