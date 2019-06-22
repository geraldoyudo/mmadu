package com.mmadu.tokenservice.controllers;

import com.mmadu.tokenservice.entities.AppToken;
import com.mmadu.tokenservice.models.CheckTokenRequest;
import com.mmadu.tokenservice.services.AppTokenService;
import com.mmadu.tokenservice.services.DomainConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
public class TokenController {
    @Autowired
    private AppTokenService appTokenService;
    @Autowired
    private DomainConfigurationService domainConfigurationService;

    @GetMapping("/generate")
    public AppToken generateToken() {
        return appTokenService.generateToken();
    }

    @GetMapping("/retrieve/{tokenId}")
    public AppToken getToken(@PathVariable("tokenId") String tokenId) {
        return appTokenService.getToken(tokenId);
    }

    @GetMapping("/reset/{tokenId}")
    public AppToken resetToken(@PathVariable("tokenId") String tokenId) {
        return appTokenService.resetToken(tokenId);
    }

    @PostMapping("/checkDomainToken")
    public boolean checkToken(@RequestBody CheckTokenRequest request) {
        return domainConfigurationService.tokenMatchesDomain(request.getTokenId(), request.getDomainId());
    }
}
