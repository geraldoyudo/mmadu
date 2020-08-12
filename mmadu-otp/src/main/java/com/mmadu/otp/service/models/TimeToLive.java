package com.mmadu.otp.service.models;

import lombok.Data;

import java.time.temporal.ChronoUnit;

@Data
public class TimeToLive {
    private long value;
    private ChronoUnit timeUnit;
}
