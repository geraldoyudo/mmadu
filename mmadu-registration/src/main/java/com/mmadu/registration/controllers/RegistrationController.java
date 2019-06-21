package com.mmadu.registration.controllers;

import com.mmadu.registration.entities.RegistrationProfile;
import com.mmadu.registration.exceptions.DomainNotFoundException;
import com.mmadu.registration.models.UserForm;
import com.mmadu.registration.providers.DomainService;
import com.mmadu.registration.providers.UserFormValidatorFactory;
import com.mmadu.registration.services.RegistrationProfileService;
import com.mmadu.registration.services.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/{domainId}")
@Slf4j
public class RegistrationController {
    @Autowired
    private RegistrationProfileService registrationProfileService;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private UserFormValidatorFactory userFormValidatorFactory;
    @Autowired
    private DomainService domainService;

    @InitBinder("user")
    public void initBinder(@PathVariable("domainId") String domainId, WebDataBinder binder) {
        binder.addValidators(userFormValidatorFactory.createValidatorForDomain(domainId));
    }

    @ModelAttribute(name = "user")
    public UserForm setUpUserForm() {
        return new UserForm();
    }

    @ModelAttribute(name = "domainId")
    public String setUpDomainId(@PathVariable("domainId") String domainId) {
        return domainId;
    }

    @ModelAttribute(name = "redirectUrl")
    public String setUpRedirectUrl(@RequestParam(name = "redirectUrl", required = false) String redirectUrl) {
        return redirectUrl;
    }

    @ModelAttribute(name = "profile")
    public RegistrationProfile setUpRegistrationProfile(@PathVariable("domainId") String domainId) {
        return registrationProfileService.getProfileForDomain(domainId);
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@PathVariable("domainId") String domainId,
                             @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                             @ModelAttribute("user") @Valid UserForm user,
                             BindingResult result,
                             @ModelAttribute("profile") RegistrationProfile profile) {
        if (!result.hasErrors()) {
            registrationService.registerUser(domainId, user);
            if (StringUtils.isEmpty(redirectUrl))
                return "redirect:" + profile.getDefaultRedirectUrl();
            else
                return "redirect:" + redirectUrl;
        }
        return "register";
    }

    @ExceptionHandler(DomainNotFoundException.class)
    public String handleDomainNotFoundException(DomainNotFoundException ex) {
        return "redirect:/html/error-404.html";
    }

    @ExceptionHandler(Exception.class)
    public String handleOtherExceptions(Exception ex) {
        log.error("unexpected error", ex);
        return "redirect:/html/error-500.html";
    }
}