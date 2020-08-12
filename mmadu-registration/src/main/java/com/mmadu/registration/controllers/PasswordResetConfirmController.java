package com.mmadu.registration.controllers;

import com.mmadu.registration.entities.DomainFlowConfiguration;
import com.mmadu.registration.exceptions.DomainNotFoundException;
import com.mmadu.registration.models.PasswordResetRequestForm;
import com.mmadu.registration.services.DomainFlowConfigurationService;
import com.mmadu.registration.services.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/{domainId}/passwordReset/confirm")
public class PasswordResetConfirmController {
    private DomainFlowConfigurationService domainFlowConfigurationService;
    private PasswordResetService passwordResetService;

    @ModelAttribute(name = "domainId")
    public String setUpDomainId(@PathVariable("domainId") String domainId) {
        return domainId;
    }

    @ModelAttribute(name = "passwordResetRequest")
    public PasswordResetRequestForm setUpPasswordResetRequestForm() {
        return new PasswordResetRequestForm();
    }

    @ModelAttribute(name = "domainConfiguration")
    public DomainFlowConfiguration setUpDomainConfiguration(@PathVariable("domainId") String domainId) {
        return domainFlowConfigurationService.findByDomainId(domainId).orElseThrow(DomainNotFoundException::new);
    }

    @GetMapping
    public String passwordReset() {
        return "password_reset_confirm";
    }

    @PostMapping
    public String doPasswordReset(@PathVariable("domainId") String domainId,
                                  @ModelAttribute("passwordResetRequest") @Valid PasswordResetRequestForm request,
                                  BindingResult result) {
        if (!result.hasErrors()) {
            passwordResetService.initiatePasswordReset(domainId, request);
            return "password_reset_initiated";
        }
        return "password_reset";
    }

    @Autowired
    public void setDomainFlowConfigurationService(DomainFlowConfigurationService domainFlowConfigurationService) {
        this.domainFlowConfigurationService = domainFlowConfigurationService;
    }

    @Autowired
    public void setPasswordResetService(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }
}
