package com.mmadu.service.providers.patchproviders;

import com.mmadu.service.model.PatchOperation;
import org.springframework.data.mongodb.core.query.Update;

public class IncrementOperation implements PatchOperationProvider{
    @Override
    public void updateRequest(String property, Object value, Update update) {
        update.inc(property, (Number) value);
    }

    @Override
    public PatchOperation operation() {
        return PatchOperation.INCREMENT;
    }
}
