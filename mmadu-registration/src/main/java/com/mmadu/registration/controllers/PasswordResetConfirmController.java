package com.mmadu.registration.controllers;

import com.mmadu.registration.entities.DomainFlowConfiguration;
import com.mmadu.registration.exceptions.DomainNotFoundException;
import com.mmadu.registration.models.PasswordResetRequestConfirmForm;
import com.mmadu.registration.services.DomainFlowConfigurationService;
import com.mmadu.registration.services.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @ModelAttribute(name = "domainConfiguration")
    public DomainFlowConfiguration setUpDomainConfiguration(@PathVariable("domainId") String domainId) {
        return domainFlowConfigurationService.findByDomainId(domainId).orElseThrow(DomainNotFoundException::new);
    }

    @GetMapping
    public String passwordResetConfirm(@RequestParam("user") String userId, @RequestParam("id") String otpId,
                                       @RequestParam("token") String token, Model model) {
        PasswordResetRequestConfirmForm form = new PasswordResetRequestConfirmForm();
        form.setUserId(userId);
        form.setOtpId(otpId);
        form.setOtpValue(token);
        model.addAttribute("passwordResetRequestConfirm", form);
        return "password_reset_confirm";
    }

    @PostMapping
    public String doPasswordResetConfirm(@PathVariable("domainId") String domainId,
                                         @ModelAttribute("passwordResetRequestConfirm") @Valid PasswordResetRequestConfirmForm request,
                                         BindingResult result) {
        if (!result.hasErrors()) {
            passwordResetService.confirmPasswordReset(domainId, request);
            return "password_reset_completed";
        }
        return "password_reset_confirm";
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
