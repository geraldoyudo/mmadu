package com.mmadu.registration.controllers;

import com.mmadu.registration.entities.RegistrationProfile;
import com.mmadu.registration.models.UserForm;
import com.mmadu.registration.services.RegistrationProfileService;
import com.mmadu.registration.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {
    @Autowired
    private RegistrationProfileService registrationProfileService;
    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/{domainId}/register")
    public String register(@PathVariable("domainId") String domainId, Model model,
                           @RequestParam(value = "redirectUrl", required = false) String redirectUrl) {
        model.addAttribute("domainId", domainId);
        model.addAttribute("user", new UserForm());
        if (!StringUtils.isEmpty(redirectUrl)) {
            model.addAttribute("redirectUrl", redirectUrl);
        }
        return "register";
    }

    @PostMapping("/{domainId}/register")
    public String doRegister(@PathVariable("domainId") String domainId,
                             @ModelAttribute UserForm user,
                             @RequestParam(value = "redirectUrl", required = false) String redirectUrl) {
        RegistrationProfile profile = registrationProfileService.getProfileForDomain(domainId);
        registrationService.registerUser(user);
        if (StringUtils.isEmpty(redirectUrl))
            return "redirect:" + profile.getDefaultRedirectUrl();
        else
            return "redirect:" + redirectUrl;
    }
}