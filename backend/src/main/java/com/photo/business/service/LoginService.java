package com.photo.business.service;

public interface LoginService {

    public String authenticateAndGetToken(String username, String password);
}
