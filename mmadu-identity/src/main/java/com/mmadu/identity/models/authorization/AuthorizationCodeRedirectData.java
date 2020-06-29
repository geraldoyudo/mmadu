package com.mmadu.identity.models.authorization;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

@Data
public class AuthorizationCodeRedirectData implements RedirectData {
    private String code;
    private String state;

    @Override
    public Map<String, List<String>> toParams() {
        Map<String, List<String>> params = new HashMap<>();
        if (!StringUtils.isEmpty(code)) {
            params.put("code", singletonList(code));
        }
        if (!StringUtils.isEmpty(state)) {
            params.put("state", singletonList(state));
        }
        return params;
    }
}
