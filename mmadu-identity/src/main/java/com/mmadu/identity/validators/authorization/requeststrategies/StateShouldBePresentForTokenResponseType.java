package com.mmadu.identity.validators.authorization.requeststrategies;

import com.mmadu.identity.models.authorization.AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

@Component
public class StateShouldBePresentForTokenResponseType implements AuthorizationRequestValidationStrategy {
    public static final String TOKEN_RESPONSE_TYPE = "token";

    @Override
    public boolean apply(AuthorizationRequest request) {
        return TOKEN_RESPONSE_TYPE.equals(request.getResponse_type());
    }

    @Override
    public void validate(AuthorizationRequest request, Errors errors) {
        if (StringUtils.isEmpty(request.getState())) {
            errors.rejectValue("state", "scope.is.required", "state parameter is required for this grant");
        }
    }
}
