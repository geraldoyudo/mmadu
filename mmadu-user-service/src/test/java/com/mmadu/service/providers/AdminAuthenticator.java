package com.mmadu.service.providers;

public interface AdminAuthenticator {

    boolean isTokenAdmin(String token);
}
