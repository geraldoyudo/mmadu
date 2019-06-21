package com.mmadu.registration.controllers;

import com.mmadu.registration.entities.RegistrationProfile;
import com.mmadu.registration.models.UserForm;
import com.mmadu.registration.providers.UserFormValidatorFactory;
import com.mmadu.registration.services.RegistrationProfileService;
import com.mmadu.registration.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/{domainId}")
public class RegistrationController {
    @Autowired
    private RegistrationProfileService registrationProfileService;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private UserFormValidatorFactory userFormValidatorFactory;

    @InitBinder
    public void initBinder(@PathVariable("domainId") String domainId, WebDataBinder binder) {
        binder.addValidators(userFormValidatorFactory.createValidatorForDomain(domainId));
    }

    @GetMapping("/register")
    public String register(@PathVariable("domainId") String domainId, Model model,
                           @RequestParam(value = "redirectUrl", required = false) String redirectUrl) {
        model.addAttribute("domainId", domainId);
        model.addAttribute("user", new UserForm());
        if (!StringUtils.isEmpty(redirectUrl)) {
            model.addAttribute("redirectUrl", redirectUrl);
        }
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@PathVariable("domainId") String domainId,
                             @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                             @ModelAttribute @Valid UserForm user,
                             BindingResult result,
                             Model model) {
        RegistrationProfile profile = registrationProfileService.getProfileForDomain(domainId);
        registrationService.registerUser(domainId, user);
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("user", user);
            model.addAttribute("domainId", domainId);
            return "register";
        }
        if (StringUtils.isEmpty(redirectUrl))
            return "redirect:" + profile.getDefaultRedirectUrl();
        else
            return "redirect:" + redirectUrl;
    }
}