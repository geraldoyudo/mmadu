package com.mmadu.identity.utils;

import java.time.ZonedDateTime;

public final class ZoneDateTimeUtils {
    private ZoneDateTimeUtils() {
    }

    public static ZonedDateTime min(ZonedDateTime first, ZonedDateTime second) {
        if (first.compareTo(second) < 0) {
            return first;
        } else {
            return second;
        }
    }

}
