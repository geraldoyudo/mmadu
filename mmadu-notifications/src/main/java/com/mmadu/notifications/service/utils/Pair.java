package com.mmadu.notifications.service.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Pair<T, V> {
    private final T first;
    private final V second;
}