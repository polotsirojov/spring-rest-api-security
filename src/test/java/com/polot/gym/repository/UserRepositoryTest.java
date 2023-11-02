package com.polot.gym.repository;

import com.polot.gym.entity.User;
import com.polot.gym.entity.enums.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsernameIgnoreCase() {
        String username = "login";
        userRepository.save(User.builder()
                .firstName("")
                .lastName("")
                .username(username)
                .password("password")
                .isActive(true)
                .role(Role.TRAINER)
                .build());
        Optional<User> userOptional = userRepository.findByUsernameIgnoreCase(username);
        Assertions.assertThat(userOptional).isPresent();
    }
}