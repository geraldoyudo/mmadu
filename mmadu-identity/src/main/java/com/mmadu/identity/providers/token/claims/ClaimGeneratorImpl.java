package com.mmadu.identity.providers.token.claims;

import com.mmadu.identity.entities.GrantAuthorization;
import com.mmadu.identity.exceptions.TokenCreationException;
import com.mmadu.identity.models.token.ClaimSpecs;
import com.mmadu.identity.models.token.TokenClaim;
import com.mmadu.identity.models.token.TokenSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class ClaimGeneratorImpl implements ClaimGenerator {
    private List<ClaimGenerationStrategy> strategies = Collections.emptyList();

    @Autowired(required = false)
    public void setStrategies(List<ClaimGenerationStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public TokenClaim generateClaim(TokenSpecification tokenSpecs, ClaimSpecs specs) {
        Optional<ClaimGenerationStrategy> strategyOptional = strategies.stream()
                .filter(s -> s.apply(tokenSpecs, specs))
                .findFirst();
        if (strategyOptional.isEmpty()) {
            throw new TokenCreationException("We do not know how to generate this claim");
        }
        return strategyOptional.get().generateClaim(tokenSpecs, specs);
    }
}
