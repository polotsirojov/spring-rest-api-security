package com.polot.gym.service.impl;

import com.polot.gym.entity.User;
import com.polot.gym.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserSession {

    private final UserRepository userRepo;

    public String getUsername() {
        return getPrincipal()
                .map(user -> user.getPrincipal().toString())
                .orElse(null);
    }

    public User getUser() {
        return userRepo.findByUsernameIgnoreCase(getUsername()).orElse(null);
    }

    public Optional<UsernamePasswordAuthenticationToken> getPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication();
        return principal instanceof UsernamePasswordAuthenticationToken ? Optional.of((UsernamePasswordAuthenticationToken) principal) : Optional.empty();
    }

}
