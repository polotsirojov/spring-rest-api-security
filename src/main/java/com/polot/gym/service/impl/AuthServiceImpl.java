package com.polot.gym.service.impl;

import com.polot.gym.entity.User;
import com.polot.gym.repository.UserRepository;
import com.polot.gym.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String authenticate(String username, String password) {
        User user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if(!passwordEncoder.matches(password,user.getPassword())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Username or password is incorrect");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        String access_token = "";
        if (authentication.isAuthenticated()) {
            access_token = jwtService.generateToken(user);
        }
        return access_token;
    }
}
