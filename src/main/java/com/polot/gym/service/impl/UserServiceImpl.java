package com.polot.gym.service.impl;

import com.polot.gym.entity.User;
import com.polot.gym.entity.enums.Role;
import com.polot.gym.payload.request.ChangeLoginPasswordRequest;
import com.polot.gym.payload.request.UserRequest;
import com.polot.gym.payload.response.UserPasswordResponse;
import com.polot.gym.payload.response.UsernamePasswordResponse;
import com.polot.gym.repository.UserRepository;
import com.polot.gym.service.AuthService;
import com.polot.gym.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @Override
    public UserPasswordResponse createUser(UserRequest request, Role role) {
        String password = generateRandomText();
        User user = userRepository.save(User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(generateRandomText())
                .password(passwordEncoder.encode(password))
                .isActive(true)
                .role(role)
                .build());
        return new UserPasswordResponse(user, password);
    }

    @Override
    public UsernamePasswordResponse login(String username, String password) {
        User user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (isUserLocked(user)) {
            throw new ResponseStatusException(HttpStatus.LOCKED, "User is locked");
        }
        try {
            String accessToken = authService.authenticate(username, password);
            return UsernamePasswordResponse.builder()
                    .accessToken(accessToken)
                    .build();
        } catch (Exception e) {
            setAttemptAndNextLoginTime(user);
            throw e;
        }
    }

    private void setAttemptAndNextLoginTime(User user) {
        user.setUnsuccessfulLoginAttempt(user.getUnsuccessfulLoginAttempt() + 1);
        if (user.getUnsuccessfulLoginAttempt() == 3) {
            user.setUnsuccessfulLoginAttempt(0);
            user.setNextLoginTime(LocalDateTime.now().plusMinutes(5));
        }
        userRepository.save(user);
    }

    private boolean isUserLocked(User user) {
        return user.getNextLoginTime() != null && user.getNextLoginTime().isAfter(LocalDateTime.now());
    }

    @Override
    public HttpEntity<Void> changeLoginPassword(ChangeLoginPasswordRequest request) {
        User user = selectByUsernameAndPassword(request.getUsername(), request.getOldPassword());
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @Override
    public User selectByUsernameAndPassword(String username, String password) {
        User user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is incorrect");
        return user;

    }

    @Override
    public User selectByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public User updateUser(User user, String firstName, String lastName, Boolean isActive) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setIsActive(isActive);
        return userRepository.save(user);
    }

    @Override
    public User updateUserStatus(User user, Boolean isActive) {
        user.setIsActive(isActive);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    private String generateRandomText() {
        String chars = "abcdefghijklmnopqrstuvwxyz";
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String NUMS = "1234567890";
        return twoRandChars(chars) + twoRandChars(CHARS) + twoRandChars(NUMS);
    }

    private String twoRandChars(String src) {
        Random rnd = new Random();
        int index1 = (int) (rnd.nextFloat() * src.length());
        int index2 = (int) (rnd.nextFloat() * src.length());
        return "" + src.charAt(index1) + src.charAt(index2);
    }
}
