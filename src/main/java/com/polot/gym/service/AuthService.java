package com.polot.gym.service;

import com.polot.gym.entity.User;

public interface AuthService {
    String authenticate(String username, String password);
}
