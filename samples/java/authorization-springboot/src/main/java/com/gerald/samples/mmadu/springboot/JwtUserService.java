package com.gerald.samples.mmadu.springboot;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        try {
            JWT jwt = JWTParser.parse(oAuth2UserRequest.getAccessToken().getTokenValue());
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            String scope = Optional.ofNullable(claims.getStringClaim("scope")).orElse("");
            return new DefaultOAuth2User(toAuthorities(scope), claims.getClaims(), "username");
        } catch (Exception ex) {
            throw new IllegalStateException("Could not parse user", ex);
        }
    }

    private List<GrantedAuthority> toAuthorities(String scope) {
        return Arrays.stream(scope.split("[ ]"))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
