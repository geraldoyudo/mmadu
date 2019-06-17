package com.mmadu.service.providers;

import com.mmadu.service.model.UpdateRequest;
import org.springframework.data.mongodb.core.query.Update;

public interface PatchOperationConverter {
    Update convertPathUpdate(UpdateRequest updateRequest);
}
