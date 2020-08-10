package com.mmadu.notifications.service.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private String code;
    private String message;
}
