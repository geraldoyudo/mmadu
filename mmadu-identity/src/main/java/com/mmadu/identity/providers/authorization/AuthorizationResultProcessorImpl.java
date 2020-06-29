package com.mmadu.identity.providers.authorization;

import com.mmadu.identity.models.authorization.AuthorizationResult;
import com.mmadu.identity.models.authorization.RedirectData;
import com.mmadu.identity.models.authorization.errors.AuthorizationError;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AuthorizationResultProcessorImpl implements AuthorizationResultProcessor {

    @Override
    public String processResult(AuthorizationResult result) {
        if (!result.isComplete()) {
            return intermediate(result.getIntermediatePage());
        } else if (result.getError() != null) {
            return error(result.getRedirectUri(), result.getError());
        } else {
            return success(result.getRedirectUri(), result.getData());
        }
    }

    private String intermediate(String intermediatePage) {
        if (intermediatePage == null || intermediatePage.isEmpty()) {
            return "authorization_page";
        } else {
            return intermediatePage;
        }
    }

    private String error(String redirectUri, AuthorizationError error) {
        Map<String, String> params = error.toParams();
        StringBuilder builder = new StringBuilder();
        builder.append("redirect:").append(redirectUri);
        if (!params.isEmpty() && !redirectUri.contains("?")) {
            builder.append("?");
        }
        if (!params.isEmpty() && redirectUri.contains("?")) {
            builder.append("&");
        }
        builder.append(
                params.entrySet()
                        .stream()
                        .map(entry -> entry.getKey() + "=" + entry.getValue())
                        .collect(Collectors.joining("&"))
        );
        return builder.toString();
    }

    private String success(String redirectUri, RedirectData data) {
        Map<String, List<String>> params = Optional.ofNullable(data.toParams()).orElse(Collections.emptyMap());
        StringBuilder builder = new StringBuilder();
        builder.append("redirect:").append(redirectUri);
        if (!params.isEmpty() && !redirectUri.contains("?")) {
            builder.append("?");
        }

        if (!params.isEmpty() && redirectUri.contains("?")) {
            builder.append("&");
        }
        builder.append(
                params.entrySet()
                        .stream()
                        .flatMap(this::flattenListEntry)
                        .collect(Collectors.joining("&"))
        );
        return builder.toString();
    }

    private Stream<String> flattenListEntry(Map.Entry<String, List<String>> listEntry) {
        return Optional.ofNullable(listEntry.getValue()).orElse(Collections.emptyList())
                .stream()
                .map(value -> listEntry.getKey() + "=" + value);
    }
}
