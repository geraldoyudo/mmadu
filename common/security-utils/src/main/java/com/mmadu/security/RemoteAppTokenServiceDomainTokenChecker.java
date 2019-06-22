package com.mmadu.security;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class RemoteAppTokenServiceDomainTokenChecker implements DomainTokenChecker {
    private String tokenServiceUrl;
    private String adminKey;
    private RestTemplate restTemplate = new RestTemplate();

    public void setTokenServiceUrl(String tokenServiceUrl) {
        this.tokenServiceUrl = tokenServiceUrl;
    }

    public void setAdminKey(String adminKey) {
        this.adminKey = adminKey;
    }

    @Override
    public boolean checkIfTokenMatchesDomainToken(String token, String domainId) {
        CheckTokenRequest request = new CheckTokenRequest();
        request.setDomainId(domainId);
        request.setTokenId(token);
        HttpHeaders headers = new HttpHeaders();
        headers.set("domain-auth-token", adminKey);
        HttpEntity<CheckTokenRequest> entity = new HttpEntity<>(request, headers);
        return restTemplate
                .exchange(UriComponentsBuilder.fromHttpUrl(tokenServiceUrl)
                                .path("/token/checkDomainToken")
                                .build().toUriString(),
                        HttpMethod.POST, entity, CheckTokenResponse.class).getBody().isMatches();
    }

    private static class CheckTokenResponse {
        private boolean matches;

        public boolean isMatches() {
            return matches;
        }

        public void setMatches(boolean matches) {
            this.matches = matches;
        }
    }
}
