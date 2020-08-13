package com.mmadu.otp.service.models;

public class ErrorResponse {
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ErrorResponse error(String code, String message) {
        ErrorResponse response = new ErrorResponse();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}
