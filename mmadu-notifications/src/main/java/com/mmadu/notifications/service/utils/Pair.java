package com.mmadu.notifications.service.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Pair<T, V> {
    private T first;
    private V second;
}