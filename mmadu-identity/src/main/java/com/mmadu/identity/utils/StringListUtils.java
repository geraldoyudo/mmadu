package com.mmadu.identity.utils;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public class StringListUtils {
    private StringListUtils() {
    }

    public static List<String> toList(String string) {
        if (string == null || string.isEmpty()) {
            return Collections.emptyList();
        }
        String[] tokens = string.trim().replaceAll(" +", " ")
                .split(" ");
        return asList(tokens);
    }

    public static String toString(List<String> items) {
        if (items == null || items.isEmpty()) {
            return "";
        } else {
            return String.join(" ", items);
        }
    }
}
