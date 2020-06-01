package com.mmadu.identity.services.authorization;

import com.mmadu.identity.models.authorization.AuthorizationContext;
import com.mmadu.identity.models.authorization.AuthorizationRequest;
import com.mmadu.identity.models.authorization.AuthorizationResponse;
import com.mmadu.identity.models.user.MmaduUser;
import com.mmadu.identity.providers.authorization.AuthorizationResultProcessor;
import com.mmadu.identity.providers.authorization.strategies.AuthorizationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    private List<AuthorizationStrategy> strategies;
    private AuthorizationResultProcessor resultProcessor;
    private HttpSession session;
    private MmaduUser mmaduUser;

    @Autowired(required = false)
    public void setStrategies(List<AuthorizationStrategy> strategies) {
        this.strategies = strategies;
    }

    @Autowired
    public void setResultProcessor(AuthorizationResultProcessor resultProcessor) {
        this.resultProcessor = resultProcessor;
    }

    @Autowired
    public void setMmaduUser(MmaduUser mmaduUser) {
        this.mmaduUser = mmaduUser;
    }

    @Autowired
    public void setSession(HttpSession session) {
        this.session = session;
    }

    @Override
    public String processAuthorization(AuthorizationRequest request, AuthorizationResponse response) {
        AuthorizationContext context = new AuthorizationContext();
        context.setAuthorizer(mmaduUser);
        List<AuthorizationStrategy> strategiesToApply = strategies.stream()
                .filter(s -> s.apply(request, response)).collect(Collectors.toList());
        for (AuthorizationStrategy strategy : strategiesToApply) {
            strategy.authorize(request, response, context);
            if (context.getResult().isComplete()) {
                break;
            }
        }
        if (strategiesToApply.stream().filter(AuthorizationStrategy::isGrantType).findFirst().isEmpty()) {
            context.set("cannotBeProcessed", true);
        }
        session.setAttribute("authorizationContext", context);
        return resultProcessor.processResult(context.getResult());
    }
}
