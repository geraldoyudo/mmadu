package com.mmadu.identity.utils;

import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

public final class MapUtils {

    private MapUtils() {

    }

    public static void putMandatory(String key, String value, Map<String, List<String>> params) {
        if (!StringUtils.isEmpty(value)) {
            params.put(key, singletonList(value));
        } else {
            throw new IllegalArgumentException(
                    String.format("%s key is required", key)
            );
        }
    }

    public static void putIfNotEmpty(String key, String value, Map<String, List<String>> params) {
        if (!StringUtils.isEmpty(value)) {
            params.put(key, singletonList(value));
        }
    }
}
