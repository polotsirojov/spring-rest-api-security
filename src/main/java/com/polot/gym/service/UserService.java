package com.polot.gym.service;

import com.polot.gym.entity.User;
import com.polot.gym.entity.enums.Role;
import com.polot.gym.payload.request.ChangeLoginPasswordRequest;
import com.polot.gym.payload.request.UserRequest;
import com.polot.gym.payload.response.UserPasswordResponse;
import com.polot.gym.payload.response.UsernamePasswordResponse;
import org.springframework.http.HttpEntity;

import java.util.Map;

public interface UserService {
    UserPasswordResponse createUser(UserRequest request, Role role);

    UsernamePasswordResponse login(String username, String password);

    HttpEntity<Void> changeLoginPassword(ChangeLoginPasswordRequest request);

    User selectByUsernameAndPassword(String username, String password);
    User selectByUsername(String username);

    User updateUser(User user, String firstName, String lastName, Boolean isActive);
    User updateUserStatus(User user, Boolean isActive);

    void deleteUser(User user);
}
