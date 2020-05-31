package com.mmadu.identity.models.authorization.errors;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class AuthorizationError {
    @NotEmpty(message = "error is required")
    private String error;
    private String errorDescription;
    private String errorUri;
    private String state;

    public Map<String, String> toParams() {
        Map<String, String> params = new HashMap<>();
        params.put("error", getError());
        putIfPresent("error_description", getErrorDescription(), params);
        putIfPresent("error_uri", getErrorUri(), params);
        putIfPresent("state", getState(), params);
        return params;
    }

    private void putIfPresent(String key, String value, Map<String, String> params) {
        if (!StringUtils.isEmpty(value)) {
            params.put(key, value);
        }
    }
}
