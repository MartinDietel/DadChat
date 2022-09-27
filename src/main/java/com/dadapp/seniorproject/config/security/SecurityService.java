package com.dadapp.seniorproject.config.security;

public interface SecurityService {
    boolean isAuthenticated();
    void autoLogin(String username, String password);
}
