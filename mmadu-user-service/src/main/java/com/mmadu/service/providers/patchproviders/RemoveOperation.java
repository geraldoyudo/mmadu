package com.mmadu.service.providers.patchproviders;

import com.mmadu.service.models.PatchOperation;
import org.springframework.data.mongodb.core.query.Update;

public class RemoveOperation implements PatchOperationProvider {
    @Override
    public void updateRequest(String property, Object value, Update update) {
        update.pull(property, value);
    }

    @Override
    public PatchOperation operation() {
        return PatchOperation.REMOVE;
    }
}
