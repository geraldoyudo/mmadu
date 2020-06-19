package com.mmadu.identity.entities.credentials;

import net.minidev.json.annotate.JsonIgnore;

public interface HasVerificationKey {
    @JsonIgnore
    byte[] getVerificationKey();
}
