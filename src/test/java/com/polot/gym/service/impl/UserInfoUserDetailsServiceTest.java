package com.polot.gym.service.impl;

import com.polot.gym.entity.User;
import com.polot.gym.entity.enums.Role;
import com.polot.gym.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserInfoUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserInfoUserDetailsService userInfoUserDetailsService;

    private User user;
    private String username = "username";
    private String password = "password";

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .username(username)
                .password(password)
                .isActive(true)
                .role(Role.TRAINER)
                .unsuccessfulLoginAttempt(0)
                .nextLoginTime(LocalDateTime.now().minusMinutes(1))
                .build();
    }

    @Test
    void loadUserByUsername() {
        given(userRepository.findByUsernameIgnoreCase(username)).willReturn(Optional.of(user));
        UserDetails userDetails = userInfoUserDetailsService.loadUserByUsername(username);
        assertThat(userDetails.getUsername()).isEqualTo(user.getUsername());
    }
}