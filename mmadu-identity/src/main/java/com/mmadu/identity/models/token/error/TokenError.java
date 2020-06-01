package com.mmadu.identity.models.token.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class TokenError {
    private String error_description;
    private String error_uri;

    public abstract String getError();
}
