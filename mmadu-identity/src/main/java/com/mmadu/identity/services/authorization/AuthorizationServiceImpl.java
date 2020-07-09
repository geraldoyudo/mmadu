package com.mmadu.identity.services.authorization;

import com.mmadu.identity.exceptions.AuthorizationException;
import com.mmadu.identity.exceptions.ClientInstanceNotFoundException;
import com.mmadu.identity.exceptions.DomainNotFoundException;
import com.mmadu.identity.models.authorization.AuthorizationContext;
import com.mmadu.identity.models.authorization.AuthorizationProfile;
import com.mmadu.identity.models.authorization.AuthorizationRequest;
import com.mmadu.identity.models.authorization.AuthorizationResponse;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.user.MmaduUser;
import com.mmadu.identity.providers.authorization.AuthorizationResultProcessor;
import com.mmadu.identity.providers.authorization.strategies.AuthorizationStrategy;
import com.mmadu.identity.services.client.MmaduClientService;
import com.mmadu.identity.services.domain.DomainIdentityConfigurationService;
import com.mmadu.identity.utils.StringListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    private List<AuthorizationStrategy> strategies;
    private AuthorizationResultProcessor resultProcessor;
    private HttpSession session;
    private HttpServletRequest httpRequest;
    private MmaduUser mmaduUser;
    private MmaduClientService mmaduClientService;
    private DomainIdentityConfigurationService domainIdentityConfigurationService;

    @Autowired(required = false)
    public void setStrategies(List<AuthorizationStrategy> strategies) {
        this.strategies = strategies;
    }

    @Autowired
    public void setResultProcessor(AuthorizationResultProcessor resultProcessor) {
        this.resultProcessor = resultProcessor;
    }

    @Autowired
    public void setDomainIdentityConfigurationService(DomainIdentityConfigurationService domainIdentityConfigurationService) {
        this.domainIdentityConfigurationService = domainIdentityConfigurationService;
    }

    @Autowired
    public void setHttpRequest(HttpServletRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Autowired
    public void setMmaduUser(MmaduUser mmaduUser) {
        this.mmaduUser = mmaduUser;
    }

    @Autowired
    public void setSession(HttpSession session) {
        this.session = session;
    }

    @Autowired
    public void setMmaduClientService(MmaduClientService mmaduClientService) {
        this.mmaduClientService = mmaduClientService;
    }

    @Override
    public String initiateAuthorization(AuthorizationRequest request) {
        MmaduClient client = mmaduClientService.loadClientByIdentifier(request.getClient_id())
                .orElseThrow(ClientInstanceNotFoundException::new);
        AuthorizationProfile profile = Optional.ofNullable(client.getAuthorizationProfile())
                .orElse(new AuthorizationProfile());
        if (profile.isAutoApproveScopes()) {
            AuthorizationResponse response = new AuthorizationResponse();
            response.setScopes(StringListUtils.toList(request.getScope()));
            response.setAuthorize(true);
            return processAuthorization(request, response);
        }
        session.setAttribute("authorizationRequest", request);
        return "authorization_page";
    }

    @Override
    public String processAuthorization(AuthorizationRequest request, AuthorizationResponse response) {
        AuthorizationContext context = new AuthorizationContext();
        context.setAuthorizer(mmaduUser);
        context.setClient(mmaduClientService.loadClientByIdentifier(request.getClient_id())
                .orElseThrow(ClientInstanceNotFoundException::new));
        context.setDomainConfiguration(domainIdentityConfigurationService.findByDomainId(mmaduUser.getDomainId())
                .orElseThrow(() -> new DomainNotFoundException("domain not found")));
        List<AuthorizationStrategy> strategiesToApply = strategies.stream()
                .filter(s -> s.apply(request, response, context)).collect(Collectors.toList());
        if (strategiesToApply.stream().filter(AuthorizationStrategy::isGrantType).findFirst().isEmpty()) {
            throw new AuthorizationException("Grant type not Supported");
        }
        for (AuthorizationStrategy strategy : strategiesToApply) {
            strategy.authorize(request, response, context);
            if (context.getResult().isComplete()) {
                break;
            }
        }
        session.setAttribute("authorizationContext", context);
        return resultProcessor.processResult(context.getResult());
    }
}
