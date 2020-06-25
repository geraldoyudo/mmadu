package com.mmadu.identity.controllers.metadata;

import com.mmadu.identity.services.metadata.VerificationKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/metadata/{domainId}")
@Slf4j
public class VerificationKeyController {
    private VerificationKeyService verificationKeyService;

    @Autowired
    public void setVerificationKeyService(VerificationKeyService verificationKeyService) {
        this.verificationKeyService = verificationKeyService;
    }

    @GetMapping("/jwks.json")
    public Map<String, Object> getKeys(@PathVariable("domainId") String domainId) {
        return verificationKeyService.getKeys(domainId);
    }
}
