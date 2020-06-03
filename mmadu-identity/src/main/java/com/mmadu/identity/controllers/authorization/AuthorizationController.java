package com.mmadu.identity.controllers.authorization;

import com.mmadu.identity.entities.Scope;
import com.mmadu.identity.models.authorization.AuthorizationRequest;
import com.mmadu.identity.models.authorization.AuthorizationResponse;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.services.client.MmaduClientService;
import com.mmadu.identity.services.user.ScopeService;
import com.mmadu.identity.services.authorization.AuthorizationService;
import com.mmadu.identity.utils.StringListUtils;
import com.mmadu.identity.validators.authorization.AuthorizationRequestValidator;
import com.mmadu.identity.validators.authorization.AuthorizationResponseValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/oauth/authorize")
public class AuthorizationController {
    private AuthorizationRequestValidator authorizationRequestValidator;
    private AuthorizationResponseValidator authorizationResponseValidator;
    private AuthorizationService authorizationService;
    private ScopeService scopeService;
    private MmaduClientService mmaduClientService;

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

    @Autowired
    public void setScopeService(ScopeService scopeService) {
        this.scopeService = scopeService;
    }

    @Autowired
    public void setMmaduClientService(MmaduClientService mmaduClientService) {
        this.mmaduClientService = mmaduClientService;
    }

    @InitBinder("authorizationRequest")
    void bindAuthorizationRequest(WebDataBinder binder) {
        binder.addValidators(authorizationRequestValidator);
    }

    @InitBinder("authorizationResponse")
    void bindAuthorizationResponse(WebDataBinder binder) {
        binder.addValidators(authorizationResponseValidator);
    }

    @ModelAttribute("availableScopes")
    public List<Scope> addScopesToModel(AuthorizationRequest request) {
        if (request.getClient_id() == null) {
            return Collections.emptyList();
        }
        Optional<MmaduClient> client = mmaduClientService.loadClientByIdentifier(request.getClient_id());
        if (client.isEmpty() && StringUtils.isEmpty(request.getScope())) {
            return Collections.emptyList();
        } else {
            return scopeService.getAllScopeInfo(
                    client.get().getDomainId(),
                    StringListUtils.toList(request.getScope())
            );
        }
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
