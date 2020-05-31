package com.mmadu.identity.controllers.authorization;

import com.mmadu.identity.models.authorization.AuthorizationRequest;
import com.mmadu.identity.models.authorization.AuthorizationResponse;
import com.mmadu.identity.services.authorization.AuthorizationService;
import com.mmadu.identity.validators.authorization.AuthorizationRequestValidator;
import com.mmadu.identity.validators.authorization.AuthorizationResponseValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("/oauth/authorize")
public class AuthorizationController {
    private AuthorizationRequestValidator authorizationRequestValidator;
    private AuthorizationResponseValidator authorizationResponseValidator;
    private AuthorizationService authorizationService;

    @Autowired
    public void setAuthorizationRequestValidator(AuthorizationRequestValidator authorizationRequestValidator) {
        this.authorizationRequestValidator = authorizationRequestValidator;
    }

    @Autowired
    public void setAuthorizationResponseValidator(AuthorizationResponseValidator authorizationResponseValidator) {
        this.authorizationResponseValidator = authorizationResponseValidator;
    }

    @Autowired
    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @InitBinder("authorizationRequest")
    void bindAuthorizationRequest(WebDataBinder binder) {
        binder.addValidators(authorizationRequestValidator);
    }

    @InitBinder("authorizationResponse")
    void bindAuthorizationResponse(WebDataBinder binder) {
        binder.addValidators(authorizationResponseValidator);
    }

    @GetMapping
    public String getAuthorizationPage(@Valid @ModelAttribute("authorizationRequest") AuthorizationRequest request,
                                       @ModelAttribute("authorizationResponse") AuthorizationResponse response,
                                       HttpSession session) {
        session.setAttribute("authorizationRequest", request);
        return "authorization_page";
    }

    @PostMapping
    public String authorize(@Valid @ModelAttribute("authorizationResponse") AuthorizationResponse response,
                            @SessionAttribute("authorizationRequest") AuthorizationRequest request) {
        log.debug("Authorizing: Request: {}, Response: {}", request, response);
        return authorizationService.processAuthorization(request, response);
    }
}
