package com.mmadu.service.providers.patchproviders;

import com.mmadu.service.models.PatchOperation;
import org.springframework.data.mongodb.core.query.Update;

public interface PatchOperationProvider {

    void updateRequest(String property, Object value, Update update);

    PatchOperation operation();
}
