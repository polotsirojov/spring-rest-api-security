package com.polot.gym.controller;

import com.polot.gym.payload.request.ChangeLoginPasswordRequest;
import com.polot.gym.payload.response.UsernamePasswordResponse;
import com.polot.gym.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("login")
    public HttpEntity<UsernamePasswordResponse> login(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(userService.login(username, password));
    }

    @PutMapping("change-password")
    public HttpEntity<Void> changeLoginPassword(@Valid @RequestBody ChangeLoginPasswordRequest request) {
        return userService.changeLoginPassword(request);
    }
}
