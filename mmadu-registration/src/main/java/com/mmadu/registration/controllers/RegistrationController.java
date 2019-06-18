package com.mmadu.registration.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RegistrationController {

    @GetMapping("/{domainId}/register")
    public String register(@PathVariable("domainId") String domainId, Model model) {
        model.addAttribute("domainId", domainId);
        return "register";
    }

    @GetMapping("/help")
    public String help() {
        return "domain/help";
    }
}