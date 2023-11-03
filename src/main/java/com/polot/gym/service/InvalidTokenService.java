package com.polot.gym.service;

import jakarta.servlet.http.HttpServletRequest;

public interface InvalidTokenService {
    void addToken(HttpServletRequest request);
}
