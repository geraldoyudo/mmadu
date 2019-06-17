package com.mmadu.service.providers.patchproviders;

import com.mmadu.service.model.PatchOperation;
import org.springframework.data.mongodb.core.query.Update;

public class AddOperation implements PatchOperationProvider {
    @Override
    public void updateRequest(String property, Object value, Update update) {
        update.addToSet(property, value);
    }

    @Override
    public PatchOperation operation() {
        return PatchOperation.ADD;
    }
}
