package com.mmadu.identity.entities.token;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface HasTokenIdentifier {

    @JsonIgnore
    String getTokenIdentifier();
}
