package com.mmadu.registration.controllers;

import com.mmadu.registration.entities.DomainFlowConfiguration;
import com.mmadu.registration.entities.RegistrationProfile;
import com.mmadu.registration.exceptions.DomainNotFoundException;
import com.mmadu.registration.models.UserForm;
import com.mmadu.registration.providers.UserFormValidatorFactory;
import com.mmadu.registration.services.DomainFlowConfigurationService;
import com.mmadu.registration.services.RegistrationProfileService;
import com.mmadu.registration.services.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/{domainId}/register/{code}")
@Slf4j
public class RegistrationController {
    @Autowired
    private RegistrationProfileService registrationProfileService;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private UserFormValidatorFactory userFormValidatorFactory;
    @Autowired
    private DomainFlowConfigurationService domainFlowConfigurationService;

    @InitBinder("user")
    public void initBinder(@PathVariable("domainId") String domainId, @PathVariable("code") String code,
                           WebDataBinder binder) {
        binder.addValidators(userFormValidatorFactory.createValidatorForDomainAndCode(domainId, code));
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
    public RegistrationProfile setUpRegistrationProfile(@PathVariable("domainId") String domainId,
                                                        @PathVariable("code") String registrationCode) {
        return registrationProfileService.getProfileForDomainAndCode(domainId, registrationCode);
    }

    @ModelAttribute(name = "proxyPath")
    public String setUpProxyPath(@RequestHeader(name = "X-Forwarded-Prefix", defaultValue = "") String proxyPath) {
        if (StringUtils.isEmpty(proxyPath)) {
            return "/";
        } else {
            return String.format("/%s/", proxyPath);
        }
    }

    @GetMapping
    public String register(@PathVariable("domainId") String domainId, Model model) {
        model.addAttribute("domain", domainId);
        model.addAttribute("domainConfiguration",
                domainFlowConfigurationService.findByDomainId(domainId).orElseThrow(DomainNotFoundException::new));
        return "register";
    }

    @PostMapping
    public String doRegister(@PathVariable("domainId") String domainId,
                             @PathVariable("code") String code,
                             @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                             @ModelAttribute("user") @Valid UserForm user,
                             BindingResult result,
                             @ModelAttribute("profile") RegistrationProfile profile) {
        if (!result.hasErrors()) {
            registrationService.registerUser(domainId, code, user);
            if (StringUtils.isEmpty(redirectUrl))
                return "redirect:" + profile.getDefaultRedirectUrl();
            else
                return "redirect:" + redirectUrl;
        }
        return "register";
    }

    @ExceptionHandler(DomainNotFoundException.class)
    public String handleDomainNotFoundException() {
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex) {
        log.error("Unexpected error", ex);
        return "error/500";
    }
}