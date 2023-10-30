package com.polot.gym.service.impl;

import com.polot.gym.config.RequestContextHolder;
import com.polot.gym.entity.User;
import com.polot.gym.entity.enums.Role;
import com.polot.gym.payload.request.ChangeLoginPasswordRequest;
import com.polot.gym.payload.request.UserRequest;
import com.polot.gym.payload.response.UserPasswordResponse;
import com.polot.gym.payload.response.UsernamePasswordResponse;
import com.polot.gym.repository.UserRepository;
import com.polot.gym.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthService authService;
    @InjectMocks
    private UserServiceImpl userService;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    private User user;
    private String username = "username";
    private String password = "password";

    @BeforeEach
    void setUp() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        RequestContextHolder.setRequest(request);
        userService = new UserServiceImpl(userRepository, passwordEncoder, authService);
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
    void createUser() {
        given(userRepository.save(any())).willReturn(user);
        UserPasswordResponse userPasswordResponse = userService.createUser(new UserRequest("firstName", "lastName"), Role.TRAINER);

        assertThat(userPasswordResponse).isNotNull();
    }

    @Test
    void login() {
        given(userRepository.findByUsernameIgnoreCase(username)).willReturn(Optional.of(user));
        given(authService.authenticate(username, password)).willReturn("token");
        UsernamePasswordResponse login = userService.login(username, password);
        assertThat(login.getAccessToken()).isEqualTo("token");
    }

    @Test
    void loginLockedUser() {
        user.setNextLoginTime(LocalDateTime.now().plusMinutes(5));
        given(userRepository.findByUsernameIgnoreCase(username)).willReturn(Optional.of(user));
        assertThatThrownBy(() -> userService.login(username, password))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("User is locked");
    }

    @Test
    void loginWithIncorrectCredentials() {
        given(userRepository.findByUsernameIgnoreCase(username)).willReturn(Optional.of(user));
        given(authService.authenticate(username, "incorrect")).willThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is incorrect"));
        assertThatThrownBy(() -> userService.login(username, "incorrect"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Username or password is incorrect");
    }

    @Test
    void changeLoginPassword() {
        given(userRepository.findByUsernameIgnoreCase(username)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(passwordEncoder.encode("newpass")).willReturn("newpass");
        userService.changeLoginPassword(new ChangeLoginPasswordRequest(username, password, "newpass"));
        then(userRepository).should().save(userArgumentCaptor.capture());
        User user1 = userArgumentCaptor.getValue();
        assertThat(user1.getPassword()).isEqualTo("newpass");
    }

    @Test
    void selectByUsernameAndPassword() {
        given(userRepository.findByUsernameIgnoreCase(username)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        User user1 = userService.selectByUsernameAndPassword(username, password);
        assertThat(user1).isEqualTo(user);
    }

    @Test
    void selectByUsername() {
        given(userRepository.findByUsernameIgnoreCase(username)).willReturn(Optional.of(user));
        User user1 = userService.selectByUsername(username);
        assertThat(user1).isEqualTo(user);
    }

    @Test
    void updateUser() {
        String newFirstname = "newfirstname";
        String newLastname = "newlastname";
        userService.updateUser(user, newFirstname, newLastname, true);
        then(userRepository).should().save(userArgumentCaptor.capture());
        User user1 = userArgumentCaptor.getValue();
        assertThat(user1.getFirstName()).isEqualTo(newFirstname);
        assertThat(user1.getLastName()).isEqualTo(newLastname);
        assertThat(user1.getIsActive()).isTrue();
    }

    @Test
    void updateUserStatus() {
        userService.updateUserStatus(user, false);
        then(userRepository).should().save(userArgumentCaptor.capture());
        User user1 = userArgumentCaptor.getValue();
        assertThat(user1.getIsActive()).isFalse();
    }

    @Test
    void deleteUser() {
        userService.deleteUser(user);
        verify(userRepository).delete(user);
    }
}