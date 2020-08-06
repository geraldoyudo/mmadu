package com.mmadu.identity.controllers.session;

import com.mmadu.identity.exceptions.DomainNotFoundException;
import com.mmadu.identity.services.domain.DomainIdentityConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Optional;

@Controller
@Slf4j
public class LoginController {
    @Value("${mmadu.identity.default-domain:0}")
    private String defaultDomain;
    private DomainIdentityConfigurationService domainIdentityConfigurationService;

    @Autowired
    public void setDomainIdentityConfigurationService(DomainIdentityConfigurationService domainIdentityConfigurationService) {
        this.domainIdentityConfigurationService = domainIdentityConfigurationService;
    }

    @GetMapping("/login")
    public String login(@SessionAttribute(name = "domain", required = false) String domain, Model model) {
        log.info("Login called: domain = {}", Optional.ofNullable(domain).orElse(defaultDomain));
        model.addAttribute("domainConfiguration",
                domainIdentityConfigurationService.findByDomainId(domain).orElseThrow(DomainNotFoundException::new));
        return "login";
    }
}
