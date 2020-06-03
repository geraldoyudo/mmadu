package com.mmadu.identity.controllers.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/app")
public class LoginController {
    @Value("${mmadu.identity.default-domain:0}")
    private String defaultDomain = "XXXX";

    @GetMapping("/login")
    public String login(@CookieValue(name = "domain", required = false) String domain) {
        log.info("Login called: domain = {}", Optional.ofNullable(domain).orElse(defaultDomain));
        return "login";
    }
}
