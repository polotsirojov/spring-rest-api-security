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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserSessionTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserSession userSession;
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
    public void testGetUser() {
        given(userRepository.findByUsernameIgnoreCase(any())).willReturn(Optional.of(user));
        User user1 = userSession.getUser();
        assertThat(user1).isEqualTo(user);
    }
}