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
        CheckTokenRequest request = createTokenRequest(token, domainId);
        HttpHeaders headers = getHeaders();
        HttpEntity<CheckTokenRequest> entity = new HttpEntity<>(request, headers);
        return restTemplate
                .exchange(UriComponentsBuilder.fromHttpUrl(tokenServiceUrl)
                                .path("/token/checkDomainToken")
                                .build().toUriString(),
                        HttpMethod.POST, entity, CheckTokenResponse.class).getBody().isMatches();
    }

    private CheckTokenRequest createTokenRequest(String token, String domainId) {
        CheckTokenRequest request = new CheckTokenRequest();
        request.setDomainId(domainId);
        request.setToken(token);
        return request;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("domain-auth-token", adminKey);
        return headers;
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

    private static class CheckTokenRequest {
        private String domainId;
        private String token;

        public String getDomainId() {
            return domainId;
        }

        public void setDomainId(String domainId) {
            this.domainId = domainId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
