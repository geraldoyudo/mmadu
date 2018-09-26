package com.mmadu.service.service;

import com.mmadu.service.entities.AppToken;

public interface AppTokenService {

    AppToken generateToken();

    AppToken refreshToken(String tokenId);

    AppToken getToken(String tokenId);

    boolean tokenMatches(String tokenId, String tokenValue);
}
