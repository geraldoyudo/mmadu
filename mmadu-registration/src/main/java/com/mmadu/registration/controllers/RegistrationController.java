package com.mmadu.registration.controllers;

import com.mmadu.registration.models.UserForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class RegistrationController {

    @GetMapping("/{domainId}/register")
    public String register(@PathVariable("domainId") String domainId, Model model) {
        model.addAttribute("domainId", domainId);
        model.addAttribute("user", new UserForm());
        return "register";
    }

    @PostMapping("/{domainId}/register")
    public String doRegister(@ModelAttribute UserForm user){
        log.info("User: {}", user);
        return "redirect:https://google.com";
    }

    @GetMapping("/help")
    public String help() {
        return "domain/help";
    }
}