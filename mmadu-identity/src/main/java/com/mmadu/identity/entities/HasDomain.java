package com.mmadu.identity.entities;

import com.mmadu.security.api.DomainPayload;

public interface HasDomain extends DomainPayload {
    String getDomainId();
}
