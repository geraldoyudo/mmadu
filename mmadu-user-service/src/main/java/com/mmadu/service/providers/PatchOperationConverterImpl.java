package com.mmadu.service.providers;

import com.mmadu.service.models.PatchOperation;
import com.mmadu.service.models.UpdateRequest;
import com.mmadu.service.providers.patchproviders.*;
import org.springframework.data.mongodb.core.query.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;

public class PatchOperationConverterImpl implements PatchOperationConverter {
    private Map<PatchOperation, PatchOperationProvider> patchOperationProviderMap = new HashMap<>();

    public PatchOperationConverterImpl() {
        this.setPatchOperations(asList(
                new SetOperation(),
                new IncrementOperation(),
                new AddOperation(),
                new RemoveOperation()
        ));
    }

    public void setPatchOperations(List<PatchOperationProvider> patchOperations) {
        patchOperations.forEach(patchOperationProvider -> {
            patchOperationProviderMap.put(patchOperationProvider.operation(), patchOperationProvider);
        });
    }

    @Override
    public Update convertPathUpdate(UpdateRequest updateRequest) {
        Update update = new Update();
        updateRequest.getUpdates()
                .forEach(userPatch -> {
                    Optional.ofNullable(patchOperationProviderMap.get(userPatch.getOperation()))
                            .ifPresent(patchOperationProvider -> {
                                patchOperationProvider.updateRequest(convertProperty(userPatch.getProperty()),
                                        userPatch.getValue(), update);
                            });
                });
        return update;
    }

    private String convertProperty(String property) {
        if (property.equals("username")) {
            return property;
        } else if (property.equals("id")) {
            return "properties.externalId";
        } else {
            return "properties." + property;
        }
    }
}
